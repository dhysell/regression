package scratchpad.cor;

import java.util.Scanner;

import random.PolicyLoader;

public class RunLoader {

    public static void main(String[] args) throws Exception {

        int swValue;

        System.out.println("========================================");
        System.out.println("|             Policy Loader            |");
        System.out.println("========================================");
        System.out.println("| Options:                             |");
        System.out.println("|        1. By Agency Name             |");
        System.out.println("|        2. By Agent Username          |");
        System.out.println("|        3. All Agents in Random Order |");
        System.out.println("|        4. Exit                       |");
        System.out.println("========================================");

        boolean validSelection = false;

        while (!validSelection) {
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(System.in);
            System.out.println("Select Option: ");
            try {
                swValue = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                swValue = 0;
            }
            switch (swValue) {
                case 1:
                    byAgency();
                    validSelection = true;
                    break;
                case 2:
                    byAgent();
                    validSelection = true;
                    break;
                case 3:
                    PolicyLoader loader = new PolicyLoader();
                    loader.policyLoaderAllAgents();
                    validSelection = true;
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    validSelection = true;
                    break;
                default:
                    System.out.println("Invalid Selection! Try Again.");
                    validSelection = false;
            }
        }

    }

    private static void byAgency() throws Exception {

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        PolicyLoader loader = new PolicyLoader();
        System.out.println("AVAILABLE AGENCIES TO CHOOSE FROM: ");
        loader.printAgencyNamesToConsole();
        System.out.println();

        System.out.println("ENTER AGENCY YOU WANT TO LOAD POLICIES FOR: ");
        String agency = scanner.nextLine();
        System.out.println("-------------------------------------------------------------------------");
        System.out.println();

        loader.setAgencyToStartWith(agency);
        System.out.println("RUNNING: POLICY LOADER STARTED FOR AGENCY: " + agency);

        try {
            loader.policyLoaderByRegion();
        } catch (Exception e) {
        }

    }

    private static void byAgent() throws Exception {

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        PolicyLoader loader = new PolicyLoader();

        System.out.println("ENTER AGENT YOU WANT TO LOAD POLICIES FOR: ");
        String agent = scanner.nextLine();
        System.out.println("-------------------------------------------------------------------------");
        System.out.println();

        loader.setAgentUsernameToStartWith(agent);
        System.out.println("RUNNING: POLICY LOADER STARTED FOR AGENT: " + agent);

        try {
            loader.policyLoaderByAgent();
        } catch (Exception e) {
//			loader.tearDown();
        }

    }

}
