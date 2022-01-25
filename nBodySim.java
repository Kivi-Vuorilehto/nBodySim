package nBodySim;
import java.io.*;
import java.util.*;

public class nBodySim {

    double simulationDuration;
    double simulationTimeIncrement;
    String nameOfFile;

    nBodySim(double simDur,double simIncrement, String dataFile) {
        simulationDuration = simDur; simulationTimeIncrement = simIncrement; nameOfFile = dataFile;

        int numberOfBodies = 0;
        double divisionConstant = 0;
        int universeRadius = 0;
        try {
            Scanner sc = new Scanner(new File(dataFile));

            if(sc.hasNextLine()) {
                numberOfBodies = Integer.parseInt(sc.nextLine());
                divisionConstant = Double.parseDouble(sc.nextLine());
                universeRadius = Integer.parseInt(sc.nextLine());

                String[] name = new String[numberOfBodies];
                Double[] xPos = new Double[numberOfBodies];
                Double[] yPos = new Double[numberOfBodies];
                Double[] xVel = new Double[numberOfBodies];
                Double[] yVel = new Double[numberOfBodies];
                Double[] mass = new Double[numberOfBodies];
                Double[] diameter = new Double[numberOfBodies];

                for (int i = 0; i < numberOfBodies; i++) {
                    String[] split = sc.nextLine().split("\\s+");
                    xPos[i] = Double.parseDouble(split[0]);
                    yPos[i] = Double.parseDouble(split[1]);
                    xVel[i] = Double.parseDouble(split[2]);
                    yVel[i] = Double.parseDouble(split[3]);
                    mass[i] = Double.parseDouble(split[4]);
                    name[i] = split[5];
                    diameter[i] = Double.parseDouble(split[6]);
                }


                System.out.println("Number of planets: " + numberOfBodies);
                System.out.println("Radius of universe: " + universeRadius);

                System.out.println(String.format("%-15s", "Init X Pos") + String.format("%-15s", "Init Y Pos") + String.format("%-15s", "Init X Vel") + String.format("%-15s", "Init Y Vel") + String.format("%-15s", "Mass") + String.format("%-15s", "Name") + String.format("%-15s", "Diameter"));
                for (int i = 0; i < numberOfBodies; i++) {
                    System.out.println(""+String.format("%-15s", xPos[i]) + String.format("%-15s", yPos[i]) + String.format("%-15s", xVel[i]) + String.format("%-15s", yVel[i]) + String.format("%-15s", mass[i]) + String.format("%-15s", name[i]) + String.format("%-15s", diameter[i]));
                }
                calculate(simulationDuration, simulationTimeIncrement, xPos, yPos, xVel, yVel, mass, name, diameter, universeRadius, divisionConstant, numberOfBodies);

            }
        }
        catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calculate(double simulationDuration, double simulationTimeIncrement ,Double[] xPos, Double[] yPos, Double[] xVel, Double[] yVel, Double[] mass, String[] name, Double[] diameter, int universeRadius, double divConst, int nOfBodies) throws InterruptedException {
        int k = 0;
        while (k < simulationDuration) {
            double[] fx = new double[nOfBodies];
            double[] fy = new double[nOfBodies];
            for (int i = 0; i < nOfBodies; i++) {
                for (int j = 0; j < nOfBodies; j++) {
                    if (i != j) {
                        double xComp = xPos[j] / 100 - xPos[i] / 100;
                        double yComp = yPos[j] / 100 - yPos[i] / 100;
                        //yComp  += -1;
                        double r = Math.sqrt((Math.pow(xComp, 2) + Math.pow(yComp, 2)));
                        double F = (6.67e-11 * mass[i] * mass[j]) / Math.pow(r, 2);
                        fx[i] += F * (xComp / r);
                        fy[i] += F * (yComp / r);
                    }
                }
            }
            for (int i = 0; i < nOfBodies; i++) {
                double ax = fx[i] / mass[i];
                double ay = fy[i] / mass[i];
                xVel[i] = xVel[i] + ax * simulationTimeIncrement;
                yVel[i] = yVel[i] + ay * simulationTimeIncrement;
                xPos[i] = xPos[i] + xVel[i] * simulationTimeIncrement;
                yPos[i] = yPos[i] + yVel[i] * simulationTimeIncrement;
            }
            k += simulationTimeIncrement;
            /*System.out.println();
            System.out.println(String.format("%-40s", "X Pos") + String.format("%-40s", "Y Pos") + String.format("%-40s", "X Vel") + String.format("%-40s", "Y Vel") + String.format("%-40s", "Mass") + String.format("%-40s", "Name") + String.format("%-40s", "Diameter"));
            for (int i = 0; i < nOfBodies; i++) {
                System.out.println(""+String.format("%-40s", xPos[i]) + String.format("%-40s", yPos[i]) + String.format("%-40s", xVel[i]) + String.format("%-40s", yVel[i]) + String.format("%-40s", mass[i]) + String.format("%-40s", name[i]) + String.format("%-40s", diameter[i]));
            }*/
            Thread.sleep(300);
            drawWorld(diameter, xPos, yPos, name, divConst, universeRadius);
        }
    }

    private void drawWorld(Double[] diameter, Double[] xPos, Double[] yPos, String[] name, double divConst, int universeRad) {
        double universeLength = (universeRad * 2) / divConst;
        //System.out.println();System.out.println();System.out.println();
        //System.out.println("----------------------------------------------");//Clear the screen


        String[][] world = new String[(int)(universeLength * 0.6)][(int)(universeLength)];
        for (String[] row: world)
            Arrays.fill(row, " ");

        for (int k = 0; k < diameter.length; k++) {
            /*
             * Uses pythagorean theorem to determine if the point
             * is in range of the radius (visualize a grid)
             */
            double rad = Math.round((diameter[k] * 0.5) / divConst);
            for (int i = 0; i < Math.round(diameter[k] / divConst); i++) {
                double x = (double) (i + .5);
                for (int j = 0; j < Math.round(diameter[k] / divConst); j++) {

                    double y = (double) (j + .5);
                    double l = ((rad - y) * (rad - y)) + ((rad - x) * (rad - x));
                    if (Math.sqrt(l) < rad) {
                        try {
                            world[(int) Math.round(yPos[k] / divConst) + j][(int) Math.round(xPos[k] / divConst) + i] = "*";
                        }
                        catch (IndexOutOfBoundsException ex){

                        }
                    }
                }
            }
        }
        System.out.println(Arrays.deepToString(world).replace("], ", "]\n"));
    }

}
