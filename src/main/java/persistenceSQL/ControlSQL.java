package persistenceSQL;
import java.sql.*;
import java.util.logging.Level;

import init.Init;
import logging.Logging;
import help.Help;

/**
 * This class provides the JDBC and handles the creation and initialization of the database
 */
public class ControlSQL {
  public Connection connection;
  private Statement stmt;
  private PreparedStatement ps;
  private ResultSet rs;

  static {
        try {
          Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
          Logging.logger.log(Level.SEVERE, "Error while loading the JDBC driver", e);
          e.printStackTrace();
        }
  }


  /**
   * The constructor creates the connection to the db and creates tables, if they do not exist already.
   */
  public ControlSQL() {
    bInitDBConnection();
    bCreateDBTables();
  }

  /**
   * Initializes the connection to the SQLite database.
   * @return True if the connection was established, false if it already was.
   */
  private boolean bInitDBConnection() {
      try {
          if (connection != null){
              return false;
          }
          else {
              connection = DriverManager.getConnection("jdbc:sqlite:" + Init.dbFile);
          }
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
          try {
            if (!connection.isClosed() && connection != null) {
                connection.close();
            }
          } catch (SQLException e) {
              e.printStackTrace();
          }
        }
    });
      return true;
  }

    /**
     * Creates two tables, if they do not already exist.
     * For reference of the section codes and titles, see:
     * <a href="http://wiki.hl7.de/index.php?title=IG:Ultrakurzformat_Patientenbezogener_Medikationsplan#Abschnitt_.E2.80.9EAktuelle_Medikation.E2.80.9C">wiki.hl7.de</a>
     * @return True if the creation was successful, false if not.
     */
  private boolean bCreateDBTables() {
    try {
      stmt = connection.createStatement();

      stmt.executeUpdate("PRAGMA foreign_keys = '1';"); // Enable foreign key support

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Medication ("
              + "pzn INTEGER PRIMARY KEY, "
              + "tradeName STRING, "
              + "substance STRING);");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Section ("
                + "sectionCode INTEGER PRIMARY KEY, "
                + "sectionTitle STRING);");

        try {
            final String sPsMedication = "INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);";

            ps = connection.prepareStatement(sPsMedication);
            ps.setString(1, "230272");
            ps.setString(2, "Metoprololsuccinat 1A");
            ps.setString(3, "95 MG");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2394397");
            ps.setString(2, "Amoxicillin RAT");
            ps.setString(3, "750 MG FTA");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1484543");
            ps.setString(2, "Prednisolon");
            ps.setString(3, "20 MG Galen");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3436979");
            ps.setString(2, "Allergodil Akut");
            ps.setString(3, "Nasenspray");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4512286");
            ps.setString(2, "Nagel Batrafen");
            ps.setString(3, "Loesung");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2083906");
            ps.setString(2, "Vivinox Sleep");
            ps.setString(3, "Schlaftab ST");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3146402");
            ps.setString(2, "Madopar LT");
            ps.setString(3, "Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "773972");
            ps.setString(2, "Torasemid 1A Pharma");
            ps.setString(3, "10 MG");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1566347");
            ps.setString(2, "Kalinor");
            ps.setString(3, "Brausetabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1969938");
            ps.setString(2, "Cipro 1A Pharma");
            ps.setString(3, "250 MG");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1578681");
            ps.setString(2, "Bepanthen");
            ps.setString(3, "Augen- und Nasensalbe");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "7387887");
            ps.setString(2, "Novaminsulfon 1A Pharma");
            ps.setString(3, "Tropfen");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1983654");
            ps.setString(2, "Ramilich");
            ps.setString(3, "10 MG Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3086350");
            ps.setString(2, "Nifedipin Ratio");
            ps.setString(3, "Weichkapseln");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "766699");
            ps.setString(2, "Ramipril 1A Pharma");
            ps.setString(3, "2.5 MG");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3634343");
            ps.setString(2, "Amnieurin 25");
            ps.setString(3, "Filmtabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4903816");
            ps.setString(2, "Digimerck Pico 0.05");
            ps.setString(3, "Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "168739");
            ps.setString(2, "Salbutamo lA L Inhalat");
            ps.setString(3, "Lösung für einen Vernebler");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2836180");
            ps.setString(2, "Atrovent 500UG/2Ml Ed Fert");
            ps.setString(3, "Inhalat");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "176101");
            ps.setString(2, "Citralopram 1A Pharma");
            ps.setString(3, "20MG");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "6488652");
            ps.setString(2, "L Thyroxin 50 1A Pharma");
            ps.setString(3, "Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "6312060");
            ps.setString(2, "Ass100 1A Pharma TAH");
            ps.setString(3, "Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "713881");
            ps.setString(2, "Budes N 0.2MG");
            ps.setString(3, "Dosierarosol");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4007393");
            ps.setString(2, "Dekristol 20000 I.E.");
            ps.setString(3, "Weichkapseln");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1014949");
            ps.setString(2, "Bisoprolol ABZ");
            ps.setString(3, "5MG");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1309142");
            ps.setString(2, "Betahistin AL 12");
            ps.setString(3, "Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "795028");
            ps.setString(2, "Metformin AL 1000");
            ps.setString(3, "Filmtabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2532741");
            ps.setString(2, "L Thyroxin 75 Henning");
            ps.setString(3, "Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3146891");
            ps.setString(2, "Nifedipin Ratio");
            ps.setString(3, "20 MG/ML Tropfen");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4274616");
            ps.setString(2, "Vomexa");
            ps.setString(3, "Dragess N");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "6313361");
            ps.setString(2, "Ibuflam");
            ps.setString(3, "400 MG Lichtenstein");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1343682");
            ps.setString(2, "ASS Ratiopharm");
            ps.setString(3, "100 MG Tabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "996330");
            ps.setString(2, "OME-Q");
            ps.setString(3, "40 MG MS ES KAPS");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4220276");
            ps.setString(2, "Citalopram Dura");
            ps.setString(3, "10MG Filmtabletten");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1202444");
            ps.setString(2, "XIPAMID 10 1 A PHARMA");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "766736");
            ps.setString(2, "RAMIPRIL 1A PHARMA 5MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3918922");
            ps.setString(2, "SPRIONOLACTON 100 HEUMANN");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "985792");
            ps.setString(2, "FUROSEMID 40 1A");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1566347");
            ps.setString(2, "KALINOR");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4537642");
            ps.setString(2, "GLIMEPIRID 1A PHARMA 1MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3075530");
            ps.setString(2, "LEVEMIR FEXPEN");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3198824");
            ps.setString(2, "CLEXANE 80MG 0.8ML");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4245916");
            ps.setString(2, "ACTRAPID PENFILL 100IE/ML");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2355262");
            ps.setString(2, "REMPIRIL RATIO COM2.5/12.5");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "7097020");
            ps.setString(2, "METOPROLOLSUCCIN AT AL 47.5");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "972884");
            ps.setString(2, "AMIODARON AL 200");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2352619");
            ps.setString(2, "AMLODIPIN AL 10 MG TABL");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3682081");
            ps.setString(2, "ENALAPRIL 20 1A PHARMA");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1970516");
            ps.setString(2, "SIMVASTATIN 1A PHARMA 20 MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "1448760");
            ps.setString(2, "SALBUTAMOL RATIO N 200H");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2223945");
            ps.setString(2, "RAMIPRIL RATIOPHARM 5MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "558736");
            ps.setString(2, "NOVORAID PENFILL ZYLINAMP");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "9900751");
            ps.setString(2, "SIMVA ARISTO 40MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2239828");
            ps.setString(2, "FENTANYL ABZ 75UG/H");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2455874");
            ps.setString(2, "LAIF 900 BALANCE");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "175231");
            ps.setString(2, "VENTOLAIR 100UG 100HUB");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "180605");
            ps.setString(2, "NSULPIRI STADA 50MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "225437");
            ps.setString(2, "AARANE N");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3651896");
            ps.setString(2, "SALBUTAMOL -CT DOSIERAEROS");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "460925");
            ps.setString(2, "TILIDIN 50/4 RETARD 1 A PHA");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "4765811");
            ps.setString(2, "FUROSEMED AL 40");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "6313415");
            ps.setString(2, "IBUFLAM 600MG LICHTENSTEIN");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "3207954");
            ps.setString(2, "EBRANTIL 60");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "5112287");
            ps.setString(2, "EXFORGE HCT 10/320/25MG");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "5387699");
            ps.setString(2, "APRIDA 100E/ML SOLOSTAR");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "5917619");
            ps.setString(2, "TRESIBA 200E/ML FLEXTOUCH");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "7717586");
            ps.setString(2, "7717586");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "8533919");
            ps.setString(2, "METOPROLOL 100 1A PHARMA");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "923360");
            ps.setString(2, "AMLODIPIN 1A PHARMA 10MG N");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "9278978");
            ps.setString(2, "KOMBOGLYZE 2.5MG/850mg");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement("INSERT OR IGNORE INTO Medication (pzn, tradeName, substance) VALUES (?,?,?);");
            ps.setString(1, "2406634");
            ps.setString(2, "CETIRIZIN AL 100MG FILMTABL");
            ps.setString(3, "");
            ps.addBatch();
            ps.executeBatch();

            //ps.close();

        } catch (SQLException e) {
            Logging.logger.log(Level.SEVERE, "Error while inserting the medications", e);
            e.printStackTrace();
            return false;
        }

        try{

          final String sPsSection = "INSERT OR IGNORE INTO Section (sectionCode, sectionTitle) VALUES (?,?);";

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "411");
            ps.setString(2, "Bedarfsmedikation");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "412");
            ps.setString(2, "Dauermedikation");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "413");
            ps.setString(2, "Intramuskuläre Anwendung");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "414");
            ps.setString(2, "Besondere Anwendung");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "415");
            ps.setString(2, "Intravenöse Anwendung");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "416");
            ps.setString(2, "Anwendung unter die Haut");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "417");
            ps.setString(2, "Fertigspritze");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "418");
            ps.setString(2, "Selbstmedikation");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "419");
            ps.setString(2, "Allergiehinweise");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "421");
            ps.setString(2, "Wichtige Hinweise");
            ps.addBatch();
            ps.executeBatch();

            ps = connection.prepareStatement(sPsSection);
            ps.setString(1, "422");
            ps.setString(2, "Wichtige Angaben");
            ps.addBatch();
            ps.executeBatch();

            ps.close();

        } catch (SQLException e) {
            Logging.logger.log(Level.SEVERE, "Error while inserting the section informations", e);
            e.printStackTrace();
            return false;
        }

      stmt.close();

    } catch (SQLException e) {
        Logging.logger.log(Level.SEVERE, "Error while creating the db tables", e);
        e.printStackTrace();
        return false;
    }
    return true;
  }

    /**
     * Crawls the database and searches for the entry with the given pzn number.
     * The resulting object contains the pzn itself, the trade name and the substance(s) of the medication.
     * @param PZN An alphanumeric pzn number as a String
     * @return A String array containing the tradeName and the substance of the medication with the given pzn number.
     */
    public PZNInfo getMedication(final String PZN) {
        PZNInfo pznInfo = new PZNInfo();

        if (connection != null){

            try {
                stmt = connection.createStatement();
                ps = connection.prepareStatement("SELECT * from Medication WHERE pzn=?;");
                ps.setString(1, PZN);
                rs = ps.executeQuery();

                if (rs.next()) { // Checks for any results and moves cursor to first row.
                    do {
                        pznInfo.setPZN(PZN);
                        pznInfo.setTradeName(rs.getString("tradeName"));
                        pznInfo.setSubstance(rs.getString("substance"));
                    }
                    while(rs.next());
                }
                rs.close();
            } catch (SQLException e1) {
                Logging.logger.log(Level.SEVERE, "Error while loading the medication with pzn no '" + PZN + "'.", e1);
                e1.printStackTrace();
            }
        }

        if (pznInfo.getTradeName() == null || pznInfo.getTradeName().isEmpty()){
            pznInfo.setTradeName("Handelsname ist unbekannt!");
        }

        return pznInfo;
    }


    /**
     * Crawls the database and searches for the entry with the given section title.
     * The resulting object contains the section code and the section title.
     * The default value of sectionCode is 0.
     * @param sectionTitle The title of the section, e.g 'Bedarfsmedikation'.
     * @return An object with the desired information.
     */
    public SectionInfo getSection(final String sectionTitle){

        int sectionCode = 0;
        String sTitle = sectionTitle;
        if (util.String.isEmpty(sectionTitle)){
            sTitle = "Standard";
        }
        if (connection != null){

            try {
                stmt = connection.createStatement();
                ps = connection.prepareStatement("SELECT * from Section WHERE sectionTitle=?;");
                ps.setString(1, sectionTitle);
                rs = ps.executeQuery();

                if (rs.next()) { // Checks for any results and moves cursor to first row.
                    do {
                        sectionCode = (rs.getInt("sectionCode"));
                    }
                    while(rs.next());
                }
                rs.close();
            } catch (SQLException e1) {
                Logging.logger.log(Level.SEVERE, "Error while loading the section with section title '" + sectionTitle + "'.", e1);
                e1.printStackTrace();
            }
        }
        return new SectionInfo(sectionCode, sTitle);
    }

    /**
     * Crawls the database and searches for the entry with the given section code.
     * The resulting object contains the section code and the section title.
     * The default value of sectionTitle is 'Standard'.
     * @param sectionCode The code of the section, e.g '418'.
     * @return An object with the desired information.
     */
    public SectionInfo getSection(final int sectionCode) {

        String sectionTitle = "Standard";
        if (connection != null){

            try {
                stmt = connection.createStatement();
                ps = connection.prepareStatement("SELECT * from Section WHERE sectionCode=?;");
                ps.setInt(1, sectionCode);
                rs = ps.executeQuery();

                if (rs.next()) { // Checks for any results and moves cursor to first row.
                    do {
                       sectionTitle = (rs.getString("sectionTitle"));
                    }
                    while(rs.next());
                }
                rs.close();
            } catch (SQLException e1) {
                Logging.logger.log(Level.SEVERE, "Error while loading the section with section code '" + sectionCode + "'.", e1);
                e1.printStackTrace();
            }
        }
        return new SectionInfo(sectionCode, sectionTitle);

    }

}
