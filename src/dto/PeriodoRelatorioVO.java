package dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Value Object (VO) que encapsula um periodo (data inicial e data final)
 * usado para a geracao de relatorios.
 *
 * Caracteristicas de um VO:
 * - Imutavel (todos os campos final, sem setters)
 * - Igualdade baseada nos valores dos atributos (equals/hashCode)
 * - Auto-validavel no construtor
 */
public final class PeriodoRelatorioVO {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LocalDate dataInicial;
    private final LocalDate dataFinal;

    public PeriodoRelatorioVO(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null) {
            throw new IllegalArgumentException("Data inicial e final sao obrigatorias.");
        }
        if (dataFinal.isBefore(dataInicial)) {
            throw new IllegalArgumentException("Data final nao pode ser anterior a data inicial.");
        }
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public boolean contem(LocalDate data) {
        if (data == null) {
            return false;
        }
        return !data.isBefore(dataInicial) && !data.isAfter(dataFinal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodoRelatorioVO)) return false;
        PeriodoRelatorioVO that = (PeriodoRelatorioVO) o;
        return Objects.equals(dataInicial, that.dataInicial)
            && Objects.equals(dataFinal, that.dataFinal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataInicial, dataFinal);
    }

    @Override
    public String toString() {
        return "Periodo " + dataInicial.format(FMT) + " ate " + dataFinal.format(FMT);
    }
}
