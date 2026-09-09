package br.com.fiap.reservas.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "br.com.fiap.reservas")
class CleanArchitectureTest {

    @ArchTest
    static final ArchRule domainMustBeFrameworkFree = noClasses()
            .that().resideInAnyPackage("br.com.fiap.reservas.domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..",
                    "jakarta.persistence..",
                    "javax.persistence..",
                    "br.com.fiap.reservas.infrastructure..",
                    "br.com.fiap.reservas.presentation..");

    @ArchTest
    static final ArchRule applicationMustNotDependOnOuterAdapters = noClasses()
            .that().resideInAnyPackage("br.com.fiap.reservas.application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "br.com.fiap.reservas.infrastructure..",
                    "br.com.fiap.reservas.presentation..");

    @ArchTest
    static final ArchRule controllersUseApplicationOnly = classes()
            .that().resideInAnyPackage("br.com.fiap.reservas.presentation.controller..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                    "br.com.fiap.reservas.presentation..",
                    "br.com.fiap.reservas.application..",
                    "java..",
                    "jakarta.validation..",
                    "org.springframework..",
                    "io.swagger..",
                    "com.fasterxml.jackson..");

    @ArchTest
    static final ArchRule localInterfacesMustUseIPrefix = classes()
            .that().areInterfaces()
            .and().resideInAnyPackage("br.com.fiap.reservas..")
            .should().haveSimpleNameStartingWith("I");

    @ArchTest
    static final ArchRule implementationNamesMustNotEndWithImpl = noClasses()
            .that().resideInAnyPackage("br.com.fiap.reservas..")
            .should().haveSimpleNameEndingWith("Impl");
}
