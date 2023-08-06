<?php
include 'conexion.php';
$id_ticket=$_GET['id_ticket'];

$consulta = "select * from ticket where id_ticket = '$id_ticket'";
$resultado = $conexion -> query($consulta);

while($fila=$resultado -> fetch_array()){
    $producto[] = array_map('utf8_encode', $fila);
}

echo json_encode($producto);
$resultado -> close();

?>