package me.itzisonn_.meazy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.itzisonn_.meazy.addons.AddonManager;
import me.itzisonn_.meazy.addons.Addon;
import me.itzisonn_.meazy.lexer.*;
import me.itzisonn_.meazy.parser.BasicParser;
import me.itzisonn_.meazy.parser.ast.DataType;
import me.itzisonn_.meazy.parser.ast.statement.Statement;
import me.itzisonn_.meazy.parser.ast.expression.*;
import me.itzisonn_.meazy.parser.ast.expression.call_expression.ClassCallExpression;
import me.itzisonn_.meazy.parser.ast.expression.call_expression.FunctionCallExpression;
import me.itzisonn_.meazy.parser.ast.expression.identifier.ClassIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.FunctionIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.identifier.VariableIdentifier;
import me.itzisonn_.meazy.parser.ast.expression.literal.BooleanLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NullLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.NumberLiteral;
import me.itzisonn_.meazy.parser.ast.expression.literal.StringLiteral;
import me.itzisonn_.meazy.parser.ast.statement.*;
import me.itzisonn_.meazy.parser.json_converters.expression.ExpressionConverter;
import me.itzisonn_.meazy.parser.json_converters.statement.StatementConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.*;
import me.itzisonn_.meazy.parser.json_converters.expression.call_expression.ClassCallExpressionConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.call_expression.FunctionCallExpressionConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.identifier.ClassIdentifierConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.identifier.FunctionIdentifierConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.identifier.VariableIdentifierConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.literal.BooleanLiteralConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.literal.NullLiteralConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.literal.NumberLiteralConverter;
import me.itzisonn_.meazy.parser.json_converters.expression.literal.StringLiteralConverter;
import me.itzisonn_.meazy.parser.json_converters.statement.*;
import me.itzisonn_.meazy.registry.RegistryIdentifier;
import me.itzisonn_.meazy.registry.Registries;
import me.itzisonn_.meazy.runtime.environment.basic.*;
import me.itzisonn_.meazy.runtime.interpreter.BasicInterpreter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public final class MeazyMain {
    @Getter
    private static final String version = "2.0";
    @Getter
    private static final MeazyMain instance = new MeazyMain();
    @Getter
    private final Logger logger = LogManager.getLogger("meazy");
    private final Gson gson = getGson();

    public static void main(String[] args) {
        instance.run(args);
    }

    private void run(String[] args) {
        if (args.length == 0) {
            logger.log(Level.ERROR, "Usage: [run <file_to_run> | compile <file_to_compile> <output_file_path> | decompile <file_to_decompile> <output_file_path>]");
            return;
        }

        File file = new File(args[1]);
        if (file.isDirectory() || !file.exists()) {
            logger.log(Level.ERROR, "File '{}' doesn't exist", file.getAbsoluteFile());
            return;
        }

        switch (args[0]) {
            case "run" -> {
                if (args.length != 2) {
                    logger.log(Level.ERROR, "Usage: run <file_to_run>");
                    return;
                }
                runFile(file);
            }
            case "compile" -> {
                if (args.length != 3) {
                    logger.log(Level.ERROR, "Usage: compile <file_to_compile> <output_file_path>");
                    return;
                }
                compileFile(file, args[2]);
            }
            case "decompile" -> {
                if (args.length != 3) {
                    logger.log(Level.ERROR, "Usage: decompile <file_to_compile> <output_file_path>");
                    return;
                }
                decompileFile(file, args[2]);
            }
            default -> logger.log(Level.ERROR, "Usage: [run <file_to_run> | compile <file_to_compile> <output_file_path> | decompile <file_to_decompile> <output_file_path>]");
        }
    }

    private void runFile(File file) {
        long startLoadMillis = System.currentTimeMillis();
        loadRegistries();
        loadAddons();
        long endLoadMillis = System.currentTimeMillis();

        logger.log(Level.INFO, "Running file '{}'", file.getAbsoluteFile());

        String extension = Utils.getExtension(file);
        long startRunMillis = System.currentTimeMillis();
        if (extension.equals("mea")) {
            ArrayList<Token> tokens = Registries.LEXER.getEntry().getValue().parseTokens(Utils.getLines(file));
            Program program = Registries.PARSER.getEntry().getValue().produceAST(tokens);
            Registries.INTERPRETER.getEntry().getValue().run(program);
        }
        else if (extension.equals("meac")) {
            Program program = gson.fromJson(Utils.getLines(file), Program.class);
            if (program == null) {
                logger.log(Level.ERROR, "Failed to read file {}, try to run it in the same version of Meazy ({})", file.getAbsolutePath(), version);
                return;
            }
            if (Utils.isVersionAfter(version, program.getVersion())) {
                logger.log(Level.ERROR, "Can't run file that has been compiled by a more recent version of the Meazy ({}), in a more older version ({})", program.getVersion(), version);
                return;
            }
            if (!version.equals(program.getVersion())) {
                logger.log(Level.WARN, "It's unsafe to run file that has been compiled by a more older version of the Meazy ({}) in a more recent version ({})", program.getVersion(), version);
                return;
            }
            Registries.INTERPRETER.getEntry().getValue().run(program);
        }
        else {
            logger.log(Level.ERROR, "Can't run file with extension {}", extension);
            return;
        }
        long endRunMillis = System.currentTimeMillis();

        System.out.println();
        logger.log(Level.INFO, "Loaded in {}s, executed in {}s",
                ((double) endLoadMillis - (double) startLoadMillis) / 1000,
                ((double) endRunMillis - (double) startRunMillis) / 1000);
    }

    private void compileFile(File file, String output) {
        if (!Utils.getExtension(file).equals("mea")) {
            logger.log(Level.ERROR, "Can't compile file with extension {}", Utils.getExtension(file));
            return;
        }

        long startLoadMillis = System.currentTimeMillis();
        loadRegistries();
        loadAddons();
        long endLoadMillis = System.currentTimeMillis();

        logger.log(Level.INFO, "Compiling file '{}'", file.getAbsoluteFile());

        long startCompileMillis = System.currentTimeMillis();
        ArrayList<Token> tokens = Registries.LEXER.getEntry().getValue().parseTokens(Utils.getLines(file));

        Program program = Registries.PARSER.getEntry().getValue().produceAST(tokens);
        long endCompileMillis = System.currentTimeMillis();


        File outputFile = new File(output);
        if (file.isDirectory()) {
            logger.log(Level.ERROR, "Output file can't be directory");
            return;
        }
        if (!outputFile.getParentFile().exists()) {
            if (outputFile.getParentFile().mkdirs()) {
                try {
                    if (outputFile.createNewFile()) logger.log(Level.INFO, "Created file '{}'", output);
                    else logger.log(Level.INFO, "File '{}' already exists", output);
                }
                catch (Exception e) {
                    logger.log(Level.ERROR, "Can't create file '{}'", output);
                    return;
                }
            }
            else {
                logger.log(Level.ERROR, "Can't create parent file");
                return;
            }
        }

        String json = gson.toJson(program, Program.class);

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(json);
            logger.log(Level.INFO, "Loaded in {}s, compiled in {}s",
                    ((double) endLoadMillis - (double) startLoadMillis) / 1000,
                    ((double) endCompileMillis - (double) startCompileMillis) / 1000);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void decompileFile(File file, String output) {
        if (!Utils.getExtension(file).equals("meac")) {
            logger.log(Level.ERROR, "Can't decompile file with extension {}", Utils.getExtension(file));
            return;
        }

        long startLoadMillis = System.currentTimeMillis();
        loadRegistries();
        loadAddons();
        long endLoadMillis = System.currentTimeMillis();

        logger.log(Level.INFO, "Decompiling file '{}'", file.getAbsoluteFile());

        long startDecompileMillis = System.currentTimeMillis();
        Program program = gson.fromJson(Utils.getLines(file), Program.class);
        if (program == null) {
            logger.log(Level.ERROR, "Failed to read file {}, try to decompile it in the same version of Meazy ({})", file.getAbsolutePath(), version);
            return;
        }
        if (Utils.isVersionAfter(version, program.getVersion())) {
            logger.log(Level.ERROR, "Can't decompile file that has been compiled by a more recent version of the Meazy ({}), in a more older version ({})", program.getVersion(), version);
            return;
        }
        if (!version.equals(program.getVersion())) {
            logger.log(Level.WARN, "It's unsafe to decompile file that has been compiled by a more older version of the Meazy ({}) in a more recent version ({})", program.getVersion(), version);
            return;
        }
        long endDecompileMillis = System.currentTimeMillis();


        File outputFile = new File(output);
        if (file.isDirectory()) {
            logger.log(Level.ERROR, "Output file can't be directory");
            return;
        }
        if (!outputFile.getParentFile().exists()) {
            if (outputFile.getParentFile().mkdirs()) {
                try {
                    if (outputFile.createNewFile()) logger.log(Level.INFO, "Created file '{}'", output);
                    else logger.log(Level.INFO, "File '{}' already exists", output);
                }
                catch (Exception e) {
                    logger.log(Level.ERROR, "Can't create file '{}'", output);
                    return;
                }
            }
            else {
                logger.log(Level.ERROR, "Can't create parent file");
                return;
            }
        }

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(program.toCodeString(0));
            logger.log(Level.INFO, "Loaded in {}s, decompiled in {}s",
                    ((double) endLoadMillis - (double) startLoadMillis) / 1000,
                    ((double) endDecompileMillis - (double) startDecompileMillis) / 1000);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void loadAddons() {
        logger.log(Level.INFO, "Loading addons...");

        File addonsDir;
        try {
            addonsDir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "/addons/");
            if (!addonsDir.exists() && !addonsDir.mkdirs()) throw new RuntimeException("Can't load addons folder");
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        AddonManager addonManager = new AddonManager();
        for (Addon addon : addonManager.loadAddons(addonsDir)) {
            addonManager.enableAddon(addon);
        }

        int addons = addonManager.getAddons().length;
        if (addons == 1) logger.log(Level.INFO, "1 addon loaded");
        else logger.log(Level.INFO, "{} addons loaded", addons);
    }



    private void loadRegistries() {
        Registries.LEXER.register(RegistryIdentifier.ofDefault("lexer"), new BasicLexer());
        Registries.PARSER.register(RegistryIdentifier.ofDefault("parser"), new BasicParser());
        Registries.INTERPRETER.register(RegistryIdentifier.ofDefault("interpreter"), new BasicInterpreter());

        Registries.GLOBAL_ENVIRONMENT.register(RegistryIdentifier.ofDefault("global_environment"), new BasicGlobalEnvironment());
        Registries.CLASS_ENVIRONMENT.register(RegistryIdentifier.ofDefault("class_environment"), BasicClassEnvironment.class);
        Registries.FUNCTION_ENVIRONMENT.register(RegistryIdentifier.ofDefault("function_environment"), BasicFunctionEnvironment.class);
        Registries.VARIABLE_DECLARATION_ENVIRONMENT.register(RegistryIdentifier.ofDefault("variable_declaration_environment"), BasicVariableDeclarationEnvironment.class);
        Registries.LOOP_ENVIRONMENT.register(RegistryIdentifier.ofDefault("loop_environment"), BasicLoopEnvironment.class);
        Registries.ENVIRONMENT.register(RegistryIdentifier.ofDefault("environment"), BasicEnvironment.class);

        TokenType.INIT();
        TokenTypeSet.INIT();
        DataType.INIT();
    }



    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(BreakStatement.class, new BreakStatementConverter())
                .registerTypeAdapter(ClassDeclarationStatement.class, new ClassDeclarationStatementConverter())
                .registerTypeAdapter(ConstructorDeclarationStatement.class, new ConstructorDeclarationStatementConverter())
                .registerTypeAdapter(ContinueStatement.class, new ContinueStatementConverter())
                .registerTypeAdapter(ForStatement.class, new ForStatementConverter())
                .registerTypeAdapter(FunctionDeclarationStatement.class, new FunctionDeclarationStatementConverter())
                .registerTypeAdapter(IfStatement.class, new IfStatementConverter())
                .registerTypeAdapter(Program.class, new ProgramConverter())
                .registerTypeAdapter(ReturnStatement.class, new ReturnStatementConverter())
                .registerTypeAdapter(VariableDeclarationStatement.class, new VariableDeclarationConverter())
                .registerTypeAdapter(WhileStatement.class, new WhileStatementConverter())

                .registerTypeAdapter(BooleanLiteral.class, new BooleanLiteralConverter())
                .registerTypeAdapter(NullLiteral.class, new NullLiteralConverter())
                .registerTypeAdapter(NumberLiteral.class, new NumberLiteralConverter())
                .registerTypeAdapter(StringLiteral.class, new StringLiteralConverter())

                .registerTypeAdapter(AssignmentExpression.class, new AssignmentExpressionConverter())
                .registerTypeAdapter(BinaryExpression.class, new BinaryExpressionConverter())
                .registerTypeAdapter(CallArgExpression.class, new CallArgExpressionConverter())
                .registerTypeAdapter(ComparisonExpression.class, new ComparisonExpressionConverter())
                .registerTypeAdapter(LogicalExpression.class, new LogicalExpressionConverter())
                .registerTypeAdapter(MemberExpression.class, new MemberExpressionConverter())
                .registerTypeAdapter(ClassCallExpression.class, new ClassCallExpressionConverter())
                .registerTypeAdapter(FunctionCallExpression.class, new FunctionCallExpressionConverter())
                .registerTypeAdapter(ClassIdentifier.class, new ClassIdentifierConverter())
                .registerTypeAdapter(FunctionIdentifier.class, new FunctionIdentifierConverter())
                .registerTypeAdapter(VariableIdentifier.class, new VariableIdentifierConverter())

                .registerTypeAdapter(Statement.class, new StatementConverter())
                .registerTypeAdapter(Expression.class, new ExpressionConverter())

                .create();
    }
}