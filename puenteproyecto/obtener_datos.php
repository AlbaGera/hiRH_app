<?php
// Configuración de la conexión a la base de datos
$servername = "localhost";
$username = "luis";
$password = "1234";
$dbname = "hirh";

// Crear conexión
$conn = new mysqli($servername, $username, $password, $dbname);

// Verificar la conexión
if ($conn->connect_error) {
    die("Error en la conexión: " . $conn->connect_error);
}

// Consulta SQL para obtener los datos de la tabla "contactos"
$sql = "SELECT * FROM ticket";
$result = $conn->query($sql);

// Verificar si se obtuvieron resultados
if ($result->num_rows > 0) {
    // Crear un array para almacenar los datos de los contactos
    $contactos = array();

    // Recorrer los resultados y agregar cada contacto al array
    while ($row = $result->fetch_assoc()) {
        $contacto = array(
            "id_ticket" => $row["id_ticket"],
            "id_empleado" => $row["id_empleado"],
            "nombre" => $row["nombre"],
            "area" => $row["area"],
            "titulo" => $row["titulo"],
            "descripcion" => $row["descripcion"]
        );
        $contactos[] = $contacto;
    }

    // Devolver los datos en formato JSON
    echo json_encode($contactos);
} else {
    echo "No se encontraron datos";
}

// Cerrar la conexión
$conn->close();
?>
