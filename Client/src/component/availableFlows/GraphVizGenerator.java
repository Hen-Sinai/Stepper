package component.availableFlows;

import DTO.*;
import step.api.DataNecessity;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

//dot -Tpng name.dot -o output.png

public class GraphVizGenerator {
    private final FlowDTO flowDTO;

    public GraphVizGenerator(FlowDTO flowDTO) {
        this.flowDTO = flowDTO;
    }

    public void generatePngFromDotFile() {
        generateDotFile(); // Generate the DOT file first

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "name.dot", "-o", flowDTO.getName() + ".png");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error generating PNG file. Exit code: " + exitCode);
            }
        } catch (IOException e) {
            System.err.println("Error executing dot command: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error waiting for dot command execution: " + e.getMessage());
        }
    }

    private void generateDotFile() {
        // Generate the DOT code based on the flowDTO object
        String dotCode = generateDotCode(flowDTO);

        // Write the DOT code to the dot file
        try {
            FileWriter writer = new FileWriter("name.dot");
            writer.write(dotCode);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing DOT file: " + e.getMessage());
        }
    }

    private String generateDotCode(FlowDTO flowDTO) {
        // Generate the DOT code based on the flowDTO object
        StringBuilder dotCodeBuilder = new StringBuilder();
        dotCodeBuilder.append("digraph Flow {\n");

        dotCodeBuilder.append(generateNodes(flowDTO));
        dotCodeBuilder.append(generateAutoMapping(flowDTO));
        dotCodeBuilder.append(generateFlowInputs(flowDTO));
        dotCodeBuilder.append(generateFlowOutputs(flowDTO));

        dotCodeBuilder.append("}");
        return dotCodeBuilder.toString();
    }

    private StringBuilder generateNodes(FlowDTO flowDTO) {
        StringBuilder sb = new StringBuilder();
        List<StepDTO> stepsList = flowDTO.getStepsDTO();
        String stepName;

        // Start the subgraph for grouping the nodes
        sb.append("  subgraph cluster_nodes {\n");
        sb.append("    style = filled;\n");
        sb.append("    color = lightblue;\n");
        sb.append("    penwidth = 2;\n");

        for (int i = 0; i < stepsList.size(); i++) {
            StepDTO step = stepsList.get(i);
            if (step.getAlias() == null) {
                stepName = step.getFinalName().replace(" ", "_");
                sb.append("    ").append(stepName).append(" [label=<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\"><TR><TD>").append(stepName).append("</TD></TR><TR><TD ALIGN=\"center\">Step Index: ").append(i + 1).append("</TD></TR></TABLE>>, style=filled, fillcolor=lightyellow];\n");
            } else {
                String name = step.getName().replace(" ", "_");
                String alias = step.getAlias().replace(" ", "_");
                stepName = "<FONT COLOR=\"green\">" + name + "</FONT><br/><FONT COLOR=\"red\">" + alias + "</FONT>";
                sb.append("    ").append(alias).append(" [label=<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\"><TR><TD>").append(stepName).append("</TD></TR><TR><TD ALIGN=\"center\">Step Index: ").append(i + 1).append("</TD></TR></TABLE>>, style=filled, fillcolor=lightyellow];\n");
            }
        }

        // Close the subgraph
        sb.append("  }\n");

        return sb;
    }


    private StringBuilder generateAutoMapping(FlowDTO flowDTO) {
        StringBuilder sb = new StringBuilder();
        List<StepDTO> stepsList = flowDTO.getStepsDTO();
        for (StepDTO step : stepsList) {
            for (StepInputDTO input : step.getInputs()) {
                if (input.getSourceStep() != null) {
                    String dest = step.getFinalName().replace(" ", "_");
                    String source = input.getSourceStep().replace(" ", "_");
                    String type = input.getType();
                    String arrowName = "<FONT COLOR=\"green\">" + type.replace(" ", "_") + "</FONT><br/><FONT COLOR=\"red\">" + input.getSourceOutput().replace(" ", "_") + "</FONT>";

                    sb.append("  ").append(source).append(" -> ").append(dest);
                    sb.append(" [label=<<FONT FACE=\"Arial\">").append(arrowName).append("</FONT>>];\n");
                }
            }
        }
        return sb;
    }



    private StringBuilder generateFlowInputs(FlowDTO flowDTO) {
        StringBuilder sb = new StringBuilder();
        for (FreeInputDTO input : flowDTO.getInputDTO()) {
            for (String dest : input.getAttachedSteps()) {
                String inputName = input.getName().replace(" ", "_");
                if (input.getNecessity() != DataNecessity.OPTIONAL) {
                    sb.append("  ").append(inputName).append(" [shape=none, label=\"").append(inputName).append("\", fontcolor=blue];\n");
                    sb.append("  ").append(inputName).append(" -> ").append(dest.replace(" ", "_")).append(" [label=<<FONT COLOR=\"blue\">").append(input.getName()).append("</FONT><BR/><FONT COLOR=\"green\">").append(input.getType()).append("</FONT>>, style=dashed, color=blue];\n");
                } else {
                    String additionalText = input.getType();
                    sb.append("  ").append(inputName).append(" [shape=none, label=<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\"><TR><TD ALIGN=\"center\"><FONT COLOR=\"green\">").append(additionalText).append("</FONT></TD></TR><TR><TD ALIGN=\"center\"><FONT COLOR=\"blue\">").append(inputName).append("</FONT></TD></TR></TABLE>>];\n");
                    sb.append("  ").append(inputName).append(" -> ").append(dest.replace(" ", "_")).append(" [label=<<FONT COLOR=\"red\">").append(input.getName()).append("</FONT><BR/><FONT COLOR=\"green\">").append(input.getType()).append("</FONT>>, style=dashed, color=blue];\n");
                }
            }
        }

        return sb;
    }

    private StringBuilder generateFlowOutputs(FlowDTO flowDTO) {
        StringBuilder sb = new StringBuilder();
        List<String> formalOutputs = flowDTO.getFormalOutputs();
        for (OutputDTO output : flowDTO.getOutputDTO()) {
            String outputName = output.getName();
            for (String formal : formalOutputs) {
                if (formal.equals(outputName)) {
                    String outputSourceStep = output.getAttachedStep();
                    String arrowId = "output_" + outputName.replace(" ", "_");
                    sb.append("  ").append(outputSourceStep.replace(" ", "_")).append(" -> ").append(arrowId).append(" [label=\"").append(outputName).append("\", style=dashed, color=green];\n");
                    sb.append("  ").append(arrowId).append(" [shape=none, label=\"").append(outputName).append("\", fontcolor=green];\n");
                }
            }
        }

        return sb;
    }
}