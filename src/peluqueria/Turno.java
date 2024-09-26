
package peluqueria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



public class Turno {
    
    private String dia;
    private String hora;
    private int clienteId;
    private int corteId;

    public Turno(String dia, String hora, int clienteId, int corteId) {
        this.dia = dia;
        this.hora = hora;
        this.clienteId = clienteId;
        this.corteId = corteId;
    }

    public Turno() {
    }
         

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getCorteId() {
        return corteId;
    }

    public void setCorteId(int corteId) {
        this.corteId = corteId;
    }
    
    public void consultarReservasT(JTable paramTablaTurno) {
        Conection conn = new Conection();
        DefaultTableModel modelo = new DefaultTableModel();
        TableRowSorter<TableModel> OrdenarTabla= new TableRowSorter<>(modelo);
        paramTablaTurno.setRowSorter(OrdenarTabla);
        
        String sql="";
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        
        paramTablaTurno.setModel(modelo);
        
        //sql= "select * from `clientes`";
        sql = "SELECT turnos.ID_Turno, clientes.nombre,clientes.apellido, turnos.dia, turnos.hora " +
              "FROM turnos " +
              "JOIN clientes ON turnos.ID_Turno = clientes.ID_cliente";
        
        
        String [] datos = new String[5]; 
        
        try  {
        
            Statement statement;
            statement = conn.conectar().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            

        
            while (resultSet.next()) {
               datos[0]=resultSet.getString(1);
               datos[1]=resultSet.getString(2);
               datos[2]=resultSet.getString(3);
               datos[3]=resultSet.getString(4);
               datos[4]=resultSet.getString(5);
               
               modelo.addRow(datos);
                
            }
            
            paramTablaTurno.setModel(modelo);
            
            
            /* String sql = "SELECT * FROM turnos";
            String sql = "SELECT turnos.ID_Turno, clientes.nombre,clientes.apellido, turnos.dia, turnos.hora " +
                         "FROM turnos " +
                         "JOIN clientes ON turnos.ID_Turno = clientes.ID_cliente";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("ID_Turno"));
                //System.out.println(resultSet.getInt("FK_Clientes"));
                //System.out.println(resultSet.getInt("FK_Cortes"));
                System.out.println("Nombre y Apellido: "+resultSet.getString("nombre")+" "+resultSet.getString("apellido"));
                
                System.out.println("fecha : "+resultSet.getString("dia"));
                System.out.println("Hora: "+resultSet.getString("hora"));
            }  
            */
            

            // Cierre del ResultSet y el Statement
            //resultSet.close();
            //statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "no se pudo mostrar los registros, error: "+e.toString());
        }

        
    }
    
    
}
