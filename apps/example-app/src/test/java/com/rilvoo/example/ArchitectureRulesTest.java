package com.rilvoo.example;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architecture guardrails for the foundation itself (test-time only, no runtime dependency):
 *  - libraries never depend on the executable application,
 *  - the shared kernel (com.rilvoo.foundation.common) stays framework-free,
 *  - controllers do not reach into persistence abstractions directly.
 */
class ArchitectureRulesTest {

    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.rilvoo");

    @Test
    void foundationLibrariesNeverDependOnTheExecutableApplication() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("com.rilvoo.foundation..")
                .should().dependOnClassesThat().resideInAPackage("com.rilvoo.example..");
        rule.check(CLASSES);
    }

    @Test
    void sharedKernelStaysFrameworkFree() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("com.rilvoo.foundation.common..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..", "jakarta.servlet..", "com.fasterxml..");
        rule.check(CLASSES);
    }

    @Test
    void controllersDoNotTouchPersistenceAbstractions() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..api..")
                .and().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                .should().dependOnClassesThat().resideInAPackage("com.rilvoo.foundation.persistence..");
        rule.check(CLASSES);
    }
}
