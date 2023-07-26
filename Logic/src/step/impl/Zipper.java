package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper extends AbstractStepDefinition {
    public Zipper() {
        super("Zipper", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.SOURCE_ZIPPER.getName(), DataNecessity.MANDATORY, "Source", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.OPERATION.getName(), DataNecessity.MANDATORY, "Operation type", DataDefinitionRegistry.ZIPPER_ENUMERATOR));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.RESULT.getName(), DataNecessity.NA, "Zip operation result", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;
        String output = null;

        try {
            String path = context.getDataValue(IO.SOURCE_ZIPPER.getName(), String.class);
            String operation = context.getDataValue(IO.OPERATION.getName(), String.class);
            Path file = Paths.get(path);

            if (operation.equals("ZIP")) {
                stepInfo.addLog("About to perform operation ZIP on source " + path);
                output = zipFile(path);
            }
            else {
                if (path.toLowerCase().endsWith(".zip"))
                    output = unzipFile(path);
                else {
                    stepInfo.addLog("File path must have a .zip extension.");
                    result = StepResult.FAILURE;
                }
            }

            context.storeDataValue(IO.RESULT.getName(), output != null ? output : "Invalid type");
        } catch (NoMatchTypeException | IOException e) {
            stepInfo.addLog(e.getMessage());
            result = StepResult.FAILURE;
            try {
                context.storeDataValue(IO.RESULT.getName(), e.getMessage());
            } catch (NoMatchTypeException e2) {
                e2.printStackTrace();
            }
        }

        stepInfo.setDuration(Duration.between(start, Instant.now()));
        stepInfo.setStepResult(result != null ? result : StepResult.SUCCESS);
        stepInfo.setSummaryLine(stepInfo.getSummaryLine() != null ? stepInfo.getSummaryLine() : "SUCCESS");
        stepInfo.setFinishTimeStamp();
        context.addStepInfo(stepInfo);
        context.dropStep();
        return stepInfo.getStepResult();
    }

    private static boolean isZipFile(Path path) {
        String fileName = path.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return extension.equalsIgnoreCase("zip");
    }

    public static String zipFile(String sourcePath) throws IOException {
        File file = new File(sourcePath);
        int dotIndex = file.getName().indexOf('.');
//        String zipFileName = file.getName() + ".zip";
        String zipFileName = dotIndex != -1 ? file.getName().substring(0, dotIndex) + ".zip" : file.getName() + ".zip";
        String destinationPath = file.getParent();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(destinationPath + File.separator + zipFileName)))) {
            if (file.isDirectory()) {
                zipDirectory(file.getPath(), file.getName(), zipOutputStream);
            } else {
                zipSingleFile(file, zipOutputStream);
            }
        }
        return "SUCCESS";
    }

    private static void zipDirectory(String directoryPath, String baseName, ZipOutputStream zipOutputStream) throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        byte[] buffer = new byte[1024];

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file.getPath(), baseName + File.separator + file.getName(), zipOutputStream);
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    zipOutputStream.putNextEntry(new ZipEntry(baseName + File.separator + file.getName()));

                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                    fis.close();
                }
            }
        }
    }

    private static void zipSingleFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(file);

        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        int length;

        while ((length = fis.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
        }
        fis.close();
    }

    public static String unzipFile(String filePath) throws IOException {
        File file = new File(filePath);
        String destinationPath = file.getParent();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(filePath)))) {
            byte[] buffer = new byte[1024];
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                File entryFile = new File(destinationPath + File.separator + entryName);

                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    new File(entryFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    int length;

                    while ((length = zipInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    fos.close();
                }

                zipEntry = zipInputStream.getNextEntry();
            }
        }

        return "SUCCESS";
    }
}