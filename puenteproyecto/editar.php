<?php
include 'conexion.php';

$id_ticket=$_POST['id_ticket'];
$id_empleado=$_POST['id_empleado'];
$nombre=$_POST['nombre'];
$area=$_POST['area'];
$titulo=$_POST['titulo'];
$descripcion=$_POST['descripcion'];


$consulta = "UPDATE TICKET SET id_empleado = '$id_empleado', nombre = '$nombre', area = '$area', titulo = '$titulo', descripcion = '$descripcion'  WHERE id_ticket = '$id_ticket'";
mysqli_query($conexion, $consulta) or die(mysqli_error($conexion));
mysqli_close($conexion);

?>
