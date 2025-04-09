package com.dota2.performance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para execução dos testes de performance usando K6
 */
public class K6TestRunner {
    private static final Logger logger = LogManager.getLogger(K6TestRunner.class);
    private static final String K6_SCRIPTS_PATH = "scripts";
    private static final String REPORT_OUTPUT_PATH = "target/k6-reports";

    /**
     * Executa todos os testes de performance K6
     * @return true se todos os testes foram executados com sucesso
     */
    public static boolean runAllTests() {
        try {
            Files.createDirectories(Paths.get(REPORT_OUTPUT_PATH));

            List<String> scriptFiles = findK6Scripts();

            if (scriptFiles.isEmpty()) {
                logger.warn("Nenhum script K6 encontrado em {}", K6_SCRIPTS_PATH);
                return false;
            }

            boolean allSuccess = true;
            for (String scriptFile : scriptFiles) {
                boolean success = runK6Script(scriptFile);
                if (!success) {
                    allSuccess = false;
                }
            }

            logger.info("Execução de testes K6 finalizada. Relatórios disponíveis em: {}", REPORT_OUTPUT_PATH);
            return allSuccess;

        } catch (IOException e) {
            logger.error("Erro ao executar testes K6: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Executa um teste K6 específico
     * @param scriptName nome do script (sem o caminho) - ex: heroes_performance.js
     * @return true se o teste foi executado com sucesso
     */
    public static boolean runTest(String scriptName) {
        try {
            Files.createDirectories(Paths.get(REPORT_OUTPUT_PATH));

            String scriptPath = K6_SCRIPTS_PATH + "/" + scriptName;
            File scriptFile = new File(scriptPath);

            if (!scriptFile.exists()) {
                logger.error("Script não encontrado: {}", scriptPath);
                return false;
            }

            boolean success = runK6Script(scriptPath);

            logger.info("Execução do teste K6 finalizada. Relatórios disponíveis em: {}", REPORT_OUTPUT_PATH);
            return success;

        } catch (IOException e) {
            logger.error("Erro ao executar teste K6: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Encontra todos os scripts K6 no diretório de scripts
     * @return lista de caminhos para os scripts
     * @throws IOException se ocorrer um erro ao acessar o diretório
     */
    private static List<String> findK6Scripts() throws IOException {
        List<String> scriptFiles = new ArrayList<>();

        File scriptsDir = new File(K6_SCRIPTS_PATH);
        if (!scriptsDir.exists() || !scriptsDir.isDirectory()) {
            logger.warn("Diretório de scripts K6 não encontrado: {}", K6_SCRIPTS_PATH);
            return scriptFiles;
        }

        Files.walk(Paths.get(K6_SCRIPTS_PATH))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".js"))
                .forEach(path -> scriptFiles.add(path.toString()));

        return scriptFiles;
    }

    /**
     * Executa um script K6 específico
     * @param scriptPath caminho para o script K6
     * @return true se o script foi executado com sucesso
     */
    private static boolean runK6Script(String scriptPath) {
        try {
            logger.info("Executando teste K6: {}", scriptPath);

            Path path = Paths.get(scriptPath);
            String baseName = path.getFileName().toString().replace(".js", "");
            String reportPath = Paths.get(REPORT_OUTPUT_PATH, baseName + "_report.html").toString();

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "k6", "run",
                    scriptPath,
                    "--out", "json=" + REPORT_OUTPUT_PATH + "/" + baseName + ".json"
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("ERROR") || line.contains("WARN") ||
                            line.contains("checks") || line.contains("http_req") ||
                            line.contains("iteration") || line.contains("vus") ||
                            line.contains("summary")) {
                        logger.info("K6: {}", line);
                    }
                }
            }

            int exitCode = process.waitFor();
            boolean success = (exitCode == 0);

            if (success) {
                logger.info("Teste K6 concluído com sucesso: {}", scriptPath);
                logger.info("Relatório HTML gerado em: {}", reportPath);
            } else {
                logger.error("Teste K6 falhou com código de saída {}: {}", exitCode, scriptPath);
            }

            return success;

        } catch (IOException | InterruptedException e) {
            logger.error("Erro ao executar o script K6 {}: {}", scriptPath, e.getMessage(), e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }
}