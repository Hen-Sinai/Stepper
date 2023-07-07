package components;

import DTO.FlowsNameDTO;
import Exceptions.*;
import engineManager.EngineManager;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LoadXml implements Runnable{
    private final EngineManager engineManager;
    private final Scanner scanner;

    public LoadXml(EngineManager engineManager, Scanner scanner) {
        this.engineManager = engineManager;
        this.scanner = scanner;
    }

    public void run() {
        String path;
        FlowsNameDTO flows;
        boolean getBack = false;

        do {
            try {
//                System.out.println("    Please enter full path to XML file");
//                System.out.println("    Enter 0 to go back");
//                path = scanner.nextLine();
//                    if (path.equals("0"))
//                        return;

                this.engineManager.loadXmlFile("C:\\Users\\chen2\\Desktop\\test\\resources\\ex1.xml");
                System.out.println("XML file load successfully");
                getBack = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: you must enter an string value.");
            } catch (JAXBException | StepNotExist | OutputNameNotUnique | FlowNameExist | NoXmlFormat | IOException |
                     StepNameNotUnique | UserInputNotFriendly | DataNotExistCustomMapping | CustomDataNotmatch |
                     ReferenceToForwardStep | DataNotExistFlowLevelAliasing | FlowOutputNotExist |
                     UserInputTypeCollision | InitialInputValueNotExist | FlowNotExist | DataNotExistContinuation |
                    InitialInputValueTypeNotMatch e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } while (!getBack);
    }
}
