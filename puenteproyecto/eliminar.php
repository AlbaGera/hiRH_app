<?php
include 'conexion.php';

$id_ticket=$_POST['id_ticket'];

$consulta = "DELETE FROM ticket WHERE id_ticket = '".$id_ticket."'";

mysqli_query($conexion, $consulta) or die(mysqli_error($conexion));
mysqli_close($conexion);
?>
