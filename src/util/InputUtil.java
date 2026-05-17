package util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utilitario para leitura segura de dados do console.
 * Faz a validacao do tipo e repete a pergunta enquanto o valor for invalido.
 */
public final class InputUtil {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtil() {
        // utilitario
    }

    public static String lerTexto(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = SCANNER.nextLine();
            if (linha != null && !linha.trim().isEmpty()) {
                return linha.trim();
            }
            System.out.println(">> Valor obrigatorio. Tente novamente.");
        }
    }

    public static String lerTextoOpcional(String prompt) {
        System.out.print(prompt);
        String linha = SCANNER.nextLine();
        return linha == null ? "" : linha.trim();
    }

    public static int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = SCANNER.nextLine();
            try {
                return Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Numero inteiro invalido. Tente novamente.");
            }
        }
    }

    public static BigDecimal lerDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = SCANNER.nextLine().trim().replace(",", ".");
            try {
                return new BigDecimal(linha);
            } catch (NumberFormatException e) {
                System.out.println(">> Valor decimal invalido. Use o formato 1234.56");
            }
        }
    }

    public static LocalDate lerData(String prompt) {
        while (true) {
            System.out.print(prompt + " (dd/MM/yyyy): ");
            String linha = SCANNER.nextLine().trim();
            try {
                return LocalDate.parse(linha, FMT);
            } catch (DateTimeParseException e) {
                System.out.println(">> Data invalida. Use o formato dd/MM/yyyy");
            }
        }
    }

    public static boolean lerSimNao(String prompt) {
        while (true) {
            System.out.print(prompt + " (S/N): ");
            String linha = SCANNER.nextLine().trim().toUpperCase();
            if (linha.equals("S") || linha.equals("SIM")) return true;
            if (linha.equals("N") || linha.equals("NAO") || linha.equals("NÃO")) return false;
            System.out.println(">> Responda com S ou N.");
        }
    }
}
