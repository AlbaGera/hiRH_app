<?php
include 'conexion.php';

$id_empleado=$_POST['id_empleado'];
$fecha_inicio=$_POST['fecha_inicio'];
$fecha_fin=$_POST['fecha_fin'];
$estatus=$_POST['estatus'];

$consulta = "INSERT INTO vacaciones(id_empleado,fecha_inicio,fecha_fin,estatus) 
values('$id_empleado','$fecha_inicio','$fecha_fin','Validando') ";

mysqli_query($conexion, $consulta) or die(mysqli_error($conexion));
mysqli_close($conexion);

?>