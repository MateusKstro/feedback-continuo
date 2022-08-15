package com.feedbackcontinuos.enums;

public enum Role {

    AGILE_COACH("Agile Coach"),
    ANALISTA_DE_DADOS("Analista de Dados"),
    ANALISTA_DE_RH("Analista de RH"),
    ANALISTA_DE_TESTES("Analista de Testes"),
    ANALISTA_DE_SUPORTE("Analista de Suporte"),
    ARQUITETO_DE_SISTEMAS("Arquiteto de Sistemas"),
    ASSISTENTE_COMERCIAL("Analista de Comercial"),
    COORDENADOR_DE_DEPARTAMENTO_PESSOAL("Coordenador de Departamento Pessoal"),
    DESENVOLVEDOR_DE_SOFTWARE("Desenvolvedor de Software"),
    ENGENHEIRO_DE_DADOS("Engenheiro de Dados"),
    ENGENHEIRO_DE_SOFTWARE("Engenheiro de Software"),
    GERENTE_DE_SOLUCOES("Gerente de Soluções"),
    GERENTE_DE_PROJETOS("Gerente de Projetos"),
    LIDER_TECNICO("Lider Técnico"),
    UX_DESIGNER("Ux Designer");

    private String role;
    Role(String role){
        this.role = role;
    }
    public String getDescription() {
        return role;
    }
}
