
package peluqueria;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Conection {

    private static final String URL = "jdbc:mysql://localhost:3306/peluqueria";
    private static final String USUARIO = "root";
    private static final String PASS="";
    Connection conn = null;

    public Conection() {
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }   
    public Connection conectar(){
        try {
            conn=DriverManager.getConnection(URL,USUARIO,PASS);
        } catch (SQLException ex) {
            Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
        }
    return conn;
    
    }
    
    /*
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASS);

    }*/
    public void crearCliente(Cliente cliente) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASS);) {
            String sql = "INSERT INTO `clientes` (`ID_cliente`, `nombre`, `apellido`, `telefono`) VALUES (NULL, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getApellido());
            statement.setString(3, cliente.getTelefono());
            statement.executeUpdate();
            System.out.println("Cliente creado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void crearTurno(Turno turno) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASS);) {
            String sql = "INSERT INTO `turnos` (`ID_Turno`, `FK_Clientes`, `FK_Cortes`, `dia`, `hora`) VALUES (NULL, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, turno.getClienteId());
            statement.setInt(2, turno.getCorteId());
            statement.setString(3, turno.getDia());
            statement.setString(4, turno.getHora());
            statement.executeUpdate();
            System.out.println("Cliente creado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void consultarReservas() {
        
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASS)) {
            // String sql = "SELECT * FROM turnos";
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
                
                //int id = resultSet.getInt("id");
                /*int clienteId = resultSet.getInt("clienteId"); // Suponemos que la columna en la tabla es "clienteId"
                String diaTurno = resultSet.getString("dia_turno");
                String horaTurno = resultSet.getString("hora_turno");

                // Obtener el cliente asociado a través del método obtenerClienteDesdeReserva
                Cliente cliente = obtenerClienteDesdeReserva(clienteId);

                // Crea un nuevo objeto Reserva con los datos obtenidos de la consulta y el cliente asociado
                Reserva reserva = new Reserva(id, cliente, diaTurno, horaTurno);
                reservas.add(reserva);*/
            }

            // Cierre del ResultSet y el Statement
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        
    }
    
 
    
    
    
    
}