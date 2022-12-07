   
function eliminarUsuario(id) {
	Swal.fire({
		title: 'Esta seguro de eliminar?',
		text: "Si elimina el usuario no se podrá recuperar!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Eliminar!',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url:"/User/delete/"+id,
				success: function(res) {
					console.log(res);
				},
			});
			//Alerta de usuario eliminado correctamente
			Swal.fire('Eliminado!', '', 'success').then((result) => {
				if (result.isConfirmed) {
					location.href = "/User/listar"
				}
			})
		}
	})
}

function eliminarEpisodio(id) {
	Swal.fire({
		title: 'Esta seguro de eliminar?',
		text: "Si elimina el episodio no se podrá recuperar!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Eliminar!',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url:"/Episodio/delete/"+id,
				success: function(res) {
					console.log(res);
				},
			});
			//Alerta de usuario eliminado correctamente
			Swal.fire('Eliminado!', '', 'success').then((result) => {
				if (result.isConfirmed) {
					location.href = "/Episodio/listar"
				}
			})
		}
	})
}




