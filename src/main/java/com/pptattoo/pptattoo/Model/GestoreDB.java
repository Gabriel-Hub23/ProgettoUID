package com.pptattoo.pptattoo.Model;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class GestoreDB {
    private static final GestoreDB instance = new GestoreDB();

    public static GestoreDB getInstance() {
        return instance;
    }
    private GestoreDB(){}

    private Connection con = null;



    public enum entità  {Dipendenti, Clienti, Appuntamenti, Servizi, Template};



    public entità getDipendenti(){ return entità.Dipendenti;}
    public entità getClienti(){ return entità.Clienti;}
    public entità getAppuntamenti(){ return entità.Appuntamenti;}
    public entità getServizi(){ return entità.Servizi;}
    public entità getTemplate(){ return entità.Template;}





    public void createConnection() throws SQLException {
        File file = new File("src/main/resources/com/pptattoo/pptattoo/db/progetto.db");
        String url = "jdbc:sqlite:"+file.getAbsolutePath();
        con = DriverManager.getConnection(url);
    }


    public void closeConnection() throws SQLException {
        if(con != null)
            con.close();
        con = null;
    }

    public ArrayList<String> leggiEntità(entità ent) throws SQLException {
        String query = "select * from "+ent.toString()+";";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        StringBuilder s= new StringBuilder();
        ArrayList<String> risultato = new ArrayList<String>();
        while(rs.next()) {
            switch (ent) {
                case Dipendenti -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Nome")).append(";").append(rs.getString("Cognome")).append(";").append(rs.getString("Ruolo")).append(";").append(rs.getString("Salario")).append(";").append(rs.getString("Username")).append(";").append(rs.getString("Password"));
                    risultato.add(s.toString());
                    s = new StringBuilder();
                }
                case Clienti -> {
                    s.append(rs.getString("CF")).append(";").append(rs.getString("Email")).append(";").append(rs.getString("Nome")).append(";").append(rs.getString("Cognome")).append(";").append(rs.getString("Numero"));
                    risultato.add(s.toString());
                    s = new StringBuilder();
                }
                case Appuntamenti -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Data")).append(";").append(rs.getString("CF_Cliente")).append(";").append(rs.getString("Id_Dipendente")).append(";").append(rs.getString("Id_Servizio"));
                    risultato.add(s.toString());
                    s = new StringBuilder();
                }
                case Servizi -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Tipo")).append(";").append(rs.getString("Prezzo"));
                    risultato.add(s.toString());
                    s = new StringBuilder();
                }
                case Template -> {
                    s.append(rs.getString("Tema")).append(";").append(rs.getString("Font")).append(";").append(rs.getString("Size")).append(";").append(rs.getString("Notifica"));
                    risultato.add(s.toString());
                    s = new StringBuilder();
                }
            }
        }
        stmt.close();
        return risultato;
    }



    public void inserimento(entità ent, String [] info) throws SQLException {
        switch (ent){
            case Dipendenti -> {
                try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO Dipendenti(Username, Password, Nome, Cognome, Salario, Ruolo) VALUES(?,?, ?, ?, ?, ?);")) {
                    pstmt.setString(1, info[1]+"."+info[2]+"."+(conta("", 1)+1));
                    pstmt.setString(2, BCrypt.hashpw(info[0], BCrypt.gensalt(12))); //Password
                    pstmt.setString(3, info[1]); //Nome
                    pstmt.setString(4, info[2]); //Cognome
                    pstmt.setDouble(5, Double.parseDouble(info[4])); //Salario
                    pstmt.setString(6, info[3]); //Ruolo
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Clienti -> {
                try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO Clienti(CF, Nome, Cognome, Numero, Email) VALUES(?,?, ?, ?, ?);")) {
                    pstmt.setString(1, info[0]); //CF
                    pstmt.setString(2, info[1]); //Nome
                    pstmt.setString(3, info[2]); //Cognome
                    pstmt.setString(4, info[3]); //Numero
                    pstmt.setString(5, info[4]); //Email
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Appuntamenti -> {
                try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO Appuntamenti(Data, CF_Cliente, Id_Dipendente, Id_Servizio) VALUES(?,?, ?, ?);")) {
                    pstmt.setString(1, info[0]); //Data
                    pstmt.setString(2, info[1]);//CF_Cliente
                    pstmt.setInt(3, Integer.parseInt(info[2])); //Id_Dipendente
                    pstmt.setInt(4, Integer.parseInt(info[3])); //Id_Servizio
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Servizi -> {
                try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO Servizi(Tipo, Prezzo) VALUES(?,?);")) {
                    pstmt.setString(1, info[0]); //Tipo
                    pstmt.setDouble(2, Double.parseDouble(info[1]));//Prezzo
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
        }
    }



    public void aggiornamento(entità ent, String [] info) throws SQLException {
        switch (ent){
            case Dipendenti -> {
                try (PreparedStatement pstmt = con.prepareStatement("UPDATE Dipendenti SET Nome = ?, Cognome = ?, Salario = ?, Ruolo = ? WHERE Id = ?;")) {
                    pstmt.setString(1, info[0]); // Nome
                    pstmt.setString(2, info[1]); // Cognome
                    pstmt.setDouble(3, Double.parseDouble(info[3])); // Salario
                    pstmt.setString(4, info[2]); // Ruolo
                    pstmt.setInt(5, Integer.parseInt(info[4])); // Id
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Clienti -> {
                try (PreparedStatement pstmt = con.prepareStatement("UPDATE Clienti SET Email = ?, Nome = ?, Cognome = ?, Numero = ? WHERE CF LIKE ?;")) {
                    pstmt.setString(1, info[3]); // Email
                    pstmt.setString(2, info[0]); // Nome
                    pstmt.setString(3, info[1]); // Cognome
                    pstmt.setString(4, info[2]); // Numero
                    pstmt.setString(5, info[4]); // CF
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Appuntamenti -> {
                try (PreparedStatement pstmt = con.prepareStatement("UPDATE Appuntamenti SET Data = ?, CF_Cliente = ?, Id_Dipendente = ?, Id_Servizio = ? WHERE Id=?;")){
                    pstmt.setString(1, info[0]); //Data
                    pstmt.setString(2, info[1]); // CF_Cliente
                    pstmt.setInt(3, Integer.parseInt(info[2])); // Id_Dipendente
                    pstmt.setInt(4, Integer.parseInt(info[3])); // Id_Servizio
                    pstmt.setInt(5, Integer.parseInt(info[4])); // Id
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Servizi -> {
                try (PreparedStatement pstmt = con.prepareStatement("UPDATE Servizi SET Tipo = ?, Prezzo = ? WHERE Id = ?;")) {
                    pstmt.setString(1, info[0]); // Tipo
                    pstmt.setDouble(2, Double.parseDouble(info[1])); // Prezzo
                    pstmt.setInt(3, Integer.parseInt(info[2])); // Id
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Template -> {
                try (PreparedStatement pstmt = con.prepareStatement("UPDATE Template SET Tema = ?, Font = ?, Notifica = ? WHERE Id = 1;")) {
                    pstmt.setString(1, info[0]); //Tema
                    pstmt.setString(2, info[1]); //Font
                    pstmt.setInt(3, Integer.parseInt(info[2])); //Notifica
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
        }
    }

    public void rimozione(entità ent, String chiave) throws SQLException {
        switch (ent){
            case Dipendenti -> {
                try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM Dipendenti WHERE Id LIKE ?;")) {
                    pstmt.setString(1, chiave);
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Clienti -> {
                try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM Clienti WHERE CF LIKE ?;")) {
                    pstmt.setString(1, chiave);
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Appuntamenti -> {
                try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM Appuntamenti WHERE Id LIKE ?;")) {
                    pstmt.setString(1, chiave);
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
            case Servizi -> {
                try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM Servizi WHERE Id LIKE ?;")) {
                    pstmt.setString(1, chiave);
                    pstmt.executeUpdate();
                } catch (SQLException e) {}
            }
        }
    }



    public String selezionaValore (entità ent, String parametro, String chiave) throws SQLException {
        String query;
        if (!ent.equals(entità.Clienti)) {
            query = "Select "+parametro+" From "+ent.toString()+" Where Id = ?;";
        }else{
            query = "Select "+parametro+" From "+ent.toString()+" Where CF LIKE ?;";
        }
        PreparedStatement stmt = con.prepareStatement(query);
        if(!ent.equals(entità.Clienti)){
            stmt.setInt(1, Integer.parseInt(chiave));
        }else{
            stmt.setString(1, chiave);
        }
        ResultSet rs = stmt.executeQuery();
        StringBuilder s= new StringBuilder();
        while(rs.next()) {
            s.append(rs.getString(parametro));
        }
        return s.toString();
    }

    public String cercaTramiteValore (entità ent, String parametro, String valore) throws SQLException {
        String query;
        query = "Select * From "+ent.toString()+" Where "+parametro+" LIKE ?;";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, valore);
        ResultSet rs = stmt.executeQuery();
        StringBuilder s= new StringBuilder();
        while(rs.next()) {
            switch (ent){
                case Dipendenti -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Nome")).append(";").append(rs.getString("Cognome")).append(";").append(rs.getString("Ruolo")).append(";").append(rs.getString("Salario")).append(";").append(rs.getString("Username")).append(";").append(rs.getString("Password"));
                }
                case Clienti -> {
                    s.append(rs.getString("CF")).append(";").append(rs.getString("Email")).append(";").append(rs.getString("Nome")).append(";").append(rs.getString("Cognome")).append(";").append(rs.getString("Numero"));
                }
                case Appuntamenti -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Data")).append(";").append(rs.getString("CF_Cliente")).append(";").append(rs.getString("Id_Dipendente")).append(";").append(rs.getString("Id_Servizio"));
                }
                case Servizi -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Tipo")).append(";").append(rs.getString("Prezzo"));
                }
            }
        }
        return s.toString();
    }

    public String cercaRiga(entità ent, String chiave) throws SQLException {
        String query;
        if (!ent.equals(entità.Clienti)) {
            query = "Select * From "+ent.toString()+" Where Id = ?;";
        }else{
            query = "Select * From "+ent.toString()+" Where CF LIKE ?;";
        }
        PreparedStatement stmt = con.prepareStatement(query);
        if(!ent.equals(entità.Clienti)){
            stmt.setInt(1, Integer.parseInt(chiave));
        }else{
            stmt.setString(1, chiave);
        }
        ResultSet rs = stmt.executeQuery();
        StringBuilder s= new StringBuilder();
        while(rs.next()) {
            switch (ent){
                case Dipendenti -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Nome")).append(";").append(rs.getString("Cognome")).append(";").append(rs.getString("Ruolo")).append(";").append(rs.getString("Salario")).append(";").append(rs.getString("Username")).append(";").append(rs.getString("Password"));
                }
                case Clienti -> {
                    s.append(rs.getString("CF")).append(";").append(rs.getString("Email")).append(";").append(rs.getString("Nome")).append(";").append(rs.getString("Cognome")).append(";").append(rs.getString("Numero"));
                }
                case Appuntamenti -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Data")).append(";").append(rs.getString("CF_Cliente")).append(";").append(rs.getString("Id_Dipendente")).append(";").append(rs.getString("Id_Servizio"));
                }
                case Servizi -> {
                    s.append(rs.getString("Id")).append(";").append(rs.getString("Tipo")).append(";").append(rs.getString("Prezzo"));
                }
            }
        }
        return s.toString();
    }



    public int conta(String data, int scelta) throws SQLException {
        String sql = "";
        switch (scelta){
            case 0 ->{
                sql = "Select Id From Appuntamenti Where Data LIKE ?;";
            }
            case 1 -> {
                sql = "Select Id From Dipendenti;";
            }
            case 2 ->{
                sql = "Select CF From Clienti;";
            }
            case 3 ->{
                sql = "Select Id From Servizi;";
            }
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        if(scelta == 0){
            stmt.setString(1, data);
        }
        ResultSet query = stmt.executeQuery();
        int s= 0;
        while(query.next()) {
            s+=1;
        }
        stmt.close();
        return s;
    }

    public void cambiaPassword(String id, String password) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE Dipendenti SET Password = ? WHERE Id = ?;")) {
            pstmt.setString(1, BCrypt.hashpw(password, BCrypt.gensalt(12)));
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }

    public int calcolaGuadagno(String data) throws SQLException {
        String sql = "Select S.Prezzo as Prezzo from Appuntamenti as A, Servizi as S where A.Id_Servizio = S.Id and A.Data LIKE ?;";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, data);
        ResultSet query = stmt.executeQuery();
        int s= 0;
        while(query.next()) {
            s+=query.getInt("Prezzo");
        }
        stmt.close();
        return s;
    }



    public ArrayList<String> creaLista(boolean cerca, String filtro, String valore) throws SQLException {
        ArrayList<String> risultato = new ArrayList<String>();
        String sql;
        if(cerca){
            sql = "Select A.Id, A.CF_Cliente, C.Email, C.Nome, C.Cognome, C.Numero, A.Data, D.Username, S.Tipo, S.Prezzo From Appuntamenti as A, Clienti as C, Dipendenti as D, Servizi as S Where A.CF_Cliente = C.CF and A.Id_Dipendente = D.Id and A.Id_Servizio = S.id and "+filtro+" LIKE ?;";
        }else{
            sql = "Select A.Id,A.CF_Cliente, C.Email, C.Nome, C.Cognome, C.Numero, A.Data, D.Username, S.Tipo, S.Prezzo From Appuntamenti as A, Clienti as C, Dipendenti as D, Servizi as S Where A.CF_Cliente = C.CF and A.Id_Dipendente = D.Id and A.Id_Servizio = S.id;";
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        if(cerca){
            stmt.setString(1,valore);
        }
        ResultSet query = stmt.executeQuery();
        String s= "";
        while(query.next()) {
            s+=query.getString("Id")+";"+query.getString("CF_Cliente")+";"+query.getString("Email")+";"+query.getString("Nome")+";"+query.getString("Cognome")+";"+query.getString("Numero")+";"+query.getString("Data")+";"+query.getString("Username")+";"+query.getString("Tipo")+";"+query.getString("Prezzo");
            risultato.add(s);
            s = "";
        }
        stmt.close();
        return risultato;
    }



    public boolean login(String username, String password) throws SQLException {
        String sql = "Select * From Dipendenti Where Username LIKE ? and Ruolo LIKE 'Proprietario' or Ruolo LIKE 'Manager';";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet query = stmt.executeQuery();
        while(query.next()) {
            if (BCrypt.checkpw(password, query.getString("Password"))){
                stmt.close();
                return true;
            }
        }
        stmt.close();
        return false;
    }

    public void svuota() throws SQLException {
        String sql;
        Statement stmt = con.createStatement();
        String [] cancella = {"Clienti", "Appuntamenti", "Servizi", "Dipendenti"};
        for(String i : cancella){
            sql = "DELETE from "+i+";";
            try{
                stmt.executeQuery(sql);
            }catch (SQLException e){}
        }
    }

}
