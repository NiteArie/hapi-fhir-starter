package com.heathcare.lab.hapistarter;

import org.mitre.synthea.engine.Generator;
import org.mitre.synthea.export.Exporter;
import org.mitre.synthea.helpers.Config;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Generate {

    public static void main(String[] args) {

        ApplicationHelper applicationHelper = new ApplicationHelper();
        applicationHelper.loadProperties();
        applicationHelper.requestAccessToken();

        Generator.GeneratorOptions options = new Generator.GeneratorOptions();
        options.population = Integer.parseInt(applicationHelper.getProperties()
                .get("openemr.population").toString());
        Config.set("exporter.fhir.export", "false");
        Config.set("exporter.hospital.fhir.export", "false");
        Config.set("exporter.practitioner.fhir.export", "false");
        Exporter.ExporterRuntimeOptions ero = new Exporter.ExporterRuntimeOptions();
        ero.enableQueue(Exporter.SupportedFhirVersion.R4);

        Generator generator = new Generator(options, ero);
        ExecutorService generatorService = Executors.newFixedThreadPool(4);
        generatorService.submit(() -> generator.run());

        int recordCount = 0;
        while (recordCount < options.population) {
            try {

                String jsonRecord = ero.getNextRecord();

                applicationHelper.createResources(applicationHelper.toJSON(jsonRecord));

                recordCount++;

            } catch (InterruptedException exception) {
                break;
            }
        }

        generatorService.shutdownNow();
    }
}


