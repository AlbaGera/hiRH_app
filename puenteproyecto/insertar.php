<?php
include 'conexion.php';

$id_empleado=$_POST['id_empleado'];
$nombre=$_POST['nombre'];
$area=$_POST['area'];
$titulo=$_POST['titulo'];
$descripcion=$_POST['descripcion'];

$consulta = "INSERT INTO ticket(id_empleado,nombre,area,titulo,descripcion) 
values('$id_empleado','$nombre','$area','$titulo','$descripcion') ";


mysqli_query($conexion, $consulta) or die(mysqli_error($conexion));
mysqli_close($conexion);

?>