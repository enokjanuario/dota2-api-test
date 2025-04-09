package com.dota2.performance;

import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;

import static org.testng.Assert.assertTrue;

/**
 * Testes de performance usando K6
 */
@Epic("Dota 2 API Testing")
@Feature("API Performance")
public class K6PerformanceTest {
    private static final Logger logger = LogManager.getLogger(K6PerformanceTest.class);
    private static final String K6_SCRIPTS_PATH = "scripts";

    @Test(description = "Executa o teste de performance para o endpoint Heroes usando K6")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Heroes API Performance")
    @Description("Este teste simula múltiplos usuários acessando o endpoint Heroes e avalia o desempenho")
    public void testHeroesPerformance() {
        if (!isK6Installed()) {
            logger.warn("K6 não está instalado ou não está disponível no PATH. Ignorando teste de performance.");
            return;
        }

        File scriptFile = new File(K6_SCRIPTS_PATH + "/heroes_performance.js");
        if (!scriptFile.exists()) {
            logger.error("Script K6 não encontrado: {}", scriptFile.getAbsolutePath());
            assertTrue(false, "Script K6 não encontrado: " + scriptFile.getAbsolutePath());
            return;
        }

        boolean success = K6TestRunner.runTest("heroes_performance.js");
        assertTrue(success, "Teste de performance para Heroes falhou");

        File reportFile = new File("heroes_performance_report.html");
        if (reportFile.exists()) {
            Allure.addAttachment("K6 Performance Report", "text/html", reportFile.getAbsolutePath());
        }
    }

    @Test(description = "Executa o teste de performance para o endpoint Matches usando K6")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Matches API Performance")
    @Description("Este teste simula múltiplos usuários acessando o endpoint Matches e avalia o desempenho")
    public void testMatchesPerformance() {
        if (!isK6Installed()) {
            logger.warn("K6 não está instalado ou não está disponível no PATH. Ignorando teste de performance.");
            return;
        }

        File scriptFile = new File(K6_SCRIPTS_PATH + "/matches_performance.js");
        if (!scriptFile.exists()) {
            logger.error("Script K6 não encontrado: {}", scriptFile.getAbsolutePath());
            assertTrue(false, "Script K6 não encontrado: " + scriptFile.getAbsolutePath());
            return;
        }

        boolean success = K6TestRunner.runTest("matches_performance.js");
        assertTrue(success, "Teste de performance para Matches falhou");

        File reportFile = new File("matches_performance_report.html");
        if (reportFile.exists()) {
            Allure.addAttachment("K6 Performance Report", "text/html", reportFile.getAbsolutePath());
        }
    }

    @Test(description = "Executa o teste de performance para os endpoints Teams e Players usando K6")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Teams and Players API Performance")
    @Description("Este teste simula múltiplos usuários acessando os endpoints Teams e Players e avalia o desempenho")
    public void testTeamsAndPlayersPerformance() {
        if (!isK6Installed()) {
            logger.warn("K6 não está instalado ou não está disponível no PATH. Ignorando teste de performance.");
            return;
        }

        File scriptFile = new File(K6_SCRIPTS_PATH + "/teams_players_performance.js");
        if (!scriptFile.exists()) {
            logger.error("Script K6 não encontrado: {}", scriptFile.getAbsolutePath());
            assertTrue(false, "Script K6 não encontrado: " + scriptFile.getAbsolutePath());
            return;
        }

        boolean success = K6TestRunner.runTest("teams_players_performance.js");
        assertTrue(success, "Teste de performance para Teams e Players falhou");

        File reportFile = new File("teams_players_performance_report.html");
        if (reportFile.exists()) {
            Allure.addAttachment("K6 Performance Report", "text/html", reportFile.getAbsolutePath());
        }
    }

    /**
     * Verifica se o K6 está instalado e disponível no PATH
     * @return true se o K6 estiver disponível
     */
    private boolean isK6Installed() {
        try {
            Process process = Runtime.getRuntime().exec("k6 version");
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            logger.warn("Erro ao verificar instalação do K6: {}", e.getMessage());
            return false;
        }
    }
}