package Exercitiu;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

class ExceptieVarsta extends Exception {
    public ExceptieVarsta(String message) {
        super( " ExceptieVarsta: " + message);
    }
}

class ExceptieAnExcursie extends Exception {
    public ExceptieAnExcursie(String message) {
        super( " ExceptieAnExcursie: " + message);
    }
}

public class MainApp {
    public static void AfisareTabelaPersoane(Connection connection) {
        String sql = "SELECT * FROM persoane";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next())
                System.out.println("id: " + resultSet.getInt(1) +
                        ", nume: " + resultSet.getString(2) +
                        ", varsta: " + resultSet.getInt(3));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void AfisareTabelaExcursii(Connection connection) {
        String sql = "SELECT * FROM excursii";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next())
                System.out.println("idExcursie:" + resultSet.getInt(1) +
                        ", idPersoana: " + resultSet.getInt(2) +
                        ", destinatia: " + resultSet.getString(3) +
                        ", anul: " + resultSet.getInt(4));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void AdaugarePersoana(Connection connection, String nume, int varsta) {
        String sql="INSERT INTO persoane(nume, varsta) VALUES(?,?)";

        try(PreparedStatement preparedStatement=connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nume);
            preparedStatement.setInt(2, varsta);
            int nrRanduri = preparedStatement.executeUpdate();
            System.out.println("\n nr. randuri afectate = " + nrRanduri);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void AdaugareExcursie(Connection connection, int idPersoana, String destinatia, int anul) {
        String sql="INSERT INTO excursii(id_persoana, destinatia, anul) VALUES(?,?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPersoana);
            preparedStatement.setString(2, destinatia);
            preparedStatement.setInt(3, anul);
            int nrRanduri = preparedStatement.executeUpdate();
            System.out.println("\n nr. randuri afectate = " + nrRanduri);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean VerificareExistentaPersInPersoane(Connection connection, int idPersoana) {
        String sql="SELECT * FROM persoane WHERE id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPersoana);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return true;
            }
            resultSet.close();
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int GetVarstaPersoana(Connection connection, int idPersoana) {
        String sql="SELECT varsta FROM persoane WHERE id = ?";
        int varsta = 18;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPersoana);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                varsta = resultSet.getInt(1);

            resultSet.close();
            return varsta;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return varsta;
    }

    public static int GetIDPersDupaNume(Connection connection, String nume) {
        String sql="SELECT id FROM persoane WHERE nume = ?";
        int id = -1;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nume);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                id = resultSet.getInt(1);

            resultSet.close();
            return id;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void AfisareExcursiiDpID(Connection connection, int idPersoana) {
        String sql = "SELECT * FROM excursii WHERE id_persoana = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPersoana);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                System.out.println("idPersoana:" + resultSet.getInt(1) +
                        ", idExcursie: " + resultSet.getInt(2) +
                        ", destinatia: " + resultSet.getString(3) +
                        ", anul: " + resultSet.getInt(4));

            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void AfisareExcursiiPersoane(Connection connection) {
        String sql = "SELECT id, nume, varsta, id_excursie, destinatia, anul FROM persoane, excursii WHERE id = id_persoana";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next())
                System.out.println( "id:" + resultSet.getInt(1) +
                        ", nume:" + resultSet.getString(2) +
                        ", varsta:" + resultSet.getInt(3) +
                        ", idExcursie:" + resultSet.getInt(4) +
                        ", destinatia: " + resultSet.getString(5) +
                        ", anul: " + resultSet.getInt(6));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void AfisarePersoane(Connection connection) {
        String sql = "SELECT id, id_excursie, destinatia, anul FROM persoane, excursii WHERE id = id_persoana";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next())
                System.out.println( "id=" + resultSet.getInt(1) +
                        ", idExcursie=" + resultSet.getInt(2) +
                        ", destinatia: " + resultSet.getString(3) +
                        ", anul: " + resultSet.getInt(4));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean AfisarePersoaneCuDest(Connection connection, String dest) {
        String sql = "SELECT id, nume, varsta, destinatia FROM persoane, excursii WHERE destinatia = ? AND id = id_persoana";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dest);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean ok = false;
            while (resultSet.next())
            {
                if(!ok) ok = true;
                System.out.println("id: " + resultSet.getInt(1) +
                        ", nume: " + resultSet.getString(2) +
                        ", varsta: " + resultSet.getInt(3) +
                        ", destinatia: " + resultSet.getString(4));
            }

            resultSet.close();
            return ok;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean AfisarePersoaneExcursieInAnul(Connection connection, int anul) {
        String sql = "SELECT id, nume, varsta, anul FROM persoane, excursii WHERE anul = ? AND id = id_persoana";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, anul);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean ok = false;
            while (resultSet.next())
            {
                if(!ok) ok = true;
                System.out.println("id: " + resultSet.getInt(1) +
                        ", nume: " + resultSet.getString(2) +
                        ", varsta: " + resultSet.getInt(3) +
                        ", anul: " + resultSet.getInt(4));
            }

            resultSet.close();
            return ok;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean StergereExcursieDpDest(Connection connection, String dest) {
        String sql = "DELETE FROM excursii WHERE destinatia = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, dest);
            int nrRanduri = preparedStatement.executeUpdate();
            System.out.println("\n nr. randuri afectate = " + nrRanduri);
            return nrRanduri != 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void StergerePersoanaCuExc(Connection connection, int id) {
        String sql = "DELETE FROM persoane WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int nrRanduri = preparedStatement.executeUpdate();
            System.out.println("\n nr. randuri afectate = " + nrRanduri);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println(" | MENU | Meniu interactiv laborator 8 PJ 27.11.2025:");
        System.out.println(" 1. Adăugarea unei persoane în tabela persoane. Numele şi vârsta se vor prelua de la\n" +
                " tastatură.\n" +
                " 2. Adăugarea unei excursii în tabela excursii. Înainte de a efectua adăugarea se va\n" +
                " verifica dacă persoana căreia îi aparține excursia a fost introdusă în tabela\n" +
                " persoane. Dacă nu a fost introdusă se va afișa un mesaj corespunzător. Datele\n" +
                " excursiei se vor prelua de la tastatură.\n" +
                " 3. Afișarea tuturor persoanelor şi pentru fiecare persoană a excursiilor în care a fost\n" +
                " 4. Afișarea excursiilor în care a fost o persoană al cărei nume se citește de la tastatură\n" +
                " 5. Afișarea tuturor persoanelor care au vizitat o anumita destinație.\n" +
                " 6. Afișarea persoanelor care au făcut excursii într-un an introdus\n" +
                " 7. Ștergerea unei excursii\n" +
                " 8. Ștergerea unei persoane (împreună cu excursiile în care a fost) \n" +
                " 9. Exit.\n");

        try {
            Scanner scanner = new Scanner(System.in);

            String url = "jdbc:mysql://localhost:3307/lab8";
            Connection connection = DriverManager.getConnection(url, "root", "root");

            int limitaVarsta = 50;

            while(true) {
                System.out.print(" | INPUT | Introd. optiunea dvs.: ");
                int opt = scanner.nextInt();

                switch (opt) {
                    case 1:
                        System.out.print(" | INPUT | Introd. numele persoanei: ");
                        String nume = scanner.next();
                        int varsta = 18;
                        try {
                            System.out.print(" | INPUT | Introd. varsta persoanei ( varsta < "+ limitaVarsta + " ): ");
                            varsta = scanner.nextInt();
                            if(varsta < 0 || varsta > limitaVarsta) {
                                varsta = 18;
                                throw new ExceptieVarsta(" Varsta invalida! ( By-def: 18 ) ");
                            }
                        }
                        catch (ExceptieVarsta exceptieVarsta) {
                            exceptieVarsta.printStackTrace();
                        }

                        AdaugarePersoana(connection, nume, varsta);
                        break;
                    case 2:
                        System.out.print(" | INPUT | Introd. idPersoana al excursiei: ");
                        int idPersoana = scanner.nextInt();

                        if(VerificareExistentaPersInPersoane(connection, idPersoana)) {
                            System.out.print(" | INPUT | Introd. destinatia excursiei: ");
                            String destinatia = scanner.next();

                            int an_curent = LocalDate.now().getYear();
                            int anul = an_curent;

                            int anul_nasterii = an_curent - GetVarstaPersoana(connection, idPersoana);

                            try {
                                System.out.print(" | INPUT | Introd. anul excursiei ( anul >= an nastere si sub anul curent ): ");
                                anul = scanner.nextInt();
                                if( anul > an_curent || anul < anul_nasterii ) {
                                    anul = an_curent;
                                    throw new ExceptieAnExcursie(" Varsta invalida! ( By-def: anul curent ) ");
                                }
                            }
                            catch (ExceptieAnExcursie exceptieAnExcursie) {
                                exceptieAnExcursie.printStackTrace();
                            }

                            AdaugareExcursie(connection, idPersoana, destinatia, anul);
                        }
                        else {
                            System.out.println(" | ERROR | Persoana inexistenta in tabela Persoane!");
                        }
                        break;
                    case 3:
                        AfisareExcursiiPersoane(connection);
                        break;
                    case 4:
                        System.out.print(" | INPUT | Introd. numele persoanei: ");
                        nume = scanner.next();
                        idPersoana = GetIDPersDupaNume(connection, nume);
                        if(idPersoana != -1) {
                            AfisareExcursiiDpID(connection, idPersoana);
                        }
                        else {
                            System.out.print(" | INVALID | Nu s-a gasit persoana in baza de date!\n");
                        }
                        break;
                    case 5:
                        System.out.print(" | INPUT | Introd. destinatia: ");
                        String destinatie = scanner.next();
                        boolean ok = AfisarePersoaneCuDest(connection, destinatie);
                        if(!ok) {
                            System.out.print(" | INVALID | Nu s-a gasit nici o excursie cu destinatia mentionata in baza de date!\n");
                        }
                        break;
                    case 6:
                        System.out.print(" | INPUT | Introd. anul excursiei: ");
                        int anul = scanner.nextInt();

                        boolean ok2 = AfisarePersoaneExcursieInAnul(connection, anul);
                        if(!ok2) {
                            System.out.print(" | INVALID | Nu s-a gasit nici o excursie cu anul mentionat in baza de date!\n");
                        }
                        break;
                    case 7:
                        System.out.print(" | INPUT | Introd. destinatia: ");
                        destinatie = scanner.next();

                        boolean ok3 = StergereExcursieDpDest(connection, destinatie);
                        if(!ok3) {
                            System.out.print(" | INVALID | Nu s-a gasit nici o excursie cu destinatia mentionata in baza de date!\n");
                        }
                        break;
                    case 8:
                        System.out.print(" | INPUT | Introd. idPersoana: ");
                        idPersoana = scanner.nextInt();

                        StergerePersoanaCuExc(connection, idPersoana);
                        break;
                    case 9:
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println(" | INVALID | Optiune invalida! Va rugam reintrod.");
                        break;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}