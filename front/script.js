// Direcciones de la API
const URL_API = "http://localhost:8080/productos";
const URL_CATEGORIAS = "http://localhost:8080/categorias";

// Elementos de la página
const lista = document.getElementById("lista");
const mensaje = document.getElementById("mensaje");
const formulario = document.getElementById("formulario");
const tituloFormulario = document.getElementById("titulo-formulario");
const botonCancelar = document.getElementById("btn-cancelar");
const filtroCategoria = document.getElementById("filtro-categoria");
const campoId = document.getElementById("id");
const campoNombre = document.getElementById("nombre");
const campoPrecio = document.getElementById("precio");
const campoStock = document.getElementById("stock");
const campoCategoria = document.getElementById("categoria");

// Productos que trajo la API (se usan para completar el formulario al editar)
let productos = [];

// ---------------------------------------------------------------
// Mensajes en pantalla
// ---------------------------------------------------------------
function mostrarMensaje(texto, esError) {
  mensaje.textContent = texto;
  mensaje.className = esError ? "mensaje error" : "mensaje exito";
  mensaje.hidden = false;
  setTimeout(() => mensaje.hidden = true, 4000);
}

// fetch NO considera error un 400 o un 404: la promesa se cumple igual.
// Por eso se revisa response.ok. La API responde los errores como texto,
// así que se leen con response.text() y se lanzan para que los tome catch.
function leerRespuesta(response) {
  if (!response.ok) {
    return response.text().then(texto => {
      throw new Error(texto);
    });
  }
  return response;
}

// ---------------------------------------------------------------
// GET categorías: completa el formulario y el filtro
// ---------------------------------------------------------------
function cargarCategorias() {
  fetch(URL_CATEGORIAS)
    .then(leerRespuesta)
    .then(response => response.json())
    .then(categorias => {
      let opcionesFormulario = `<option value="">Seleccione una categoría</option>`;
      let opcionesFiltro = `<option value="">Todas</option>`;

      categorias.forEach(c => {
        opcionesFormulario += `<option value="${c.id}">${c.nombre}</option>`;
        opcionesFiltro += `<option value="${c.nombre}">${c.nombre}</option>`;
      });

      campoCategoria.innerHTML = opcionesFormulario;
      filtroCategoria.innerHTML = opcionesFiltro;
    })
    .catch(error => mostrarMensaje(error.message, true));
}

// ---------------------------------------------------------------
// GET productos: todos, o los de la categoría elegida en el filtro
// ---------------------------------------------------------------
function cargarProductos() {
  let url = URL_API;

  if (filtroCategoria.value !== "") {
    // encodeURIComponent convierte espacios y tildes para que viajen bien en la URL
    url = URL_API + "/categoria/" + encodeURIComponent(filtroCategoria.value);
  }

  fetch(url)
    .then(leerRespuesta)
    .then(response => response.json())
    .then(datos => {
      productos = datos;
      mostrarProductos();
    })
    .catch(error => mostrarMensaje(error.message, true));
}

function mostrarProductos() {
  if (productos.length === 0) {
    lista.innerHTML = "<p>No hay productos para mostrar.</p>";
    return;
  }

  let html = "";

  productos.forEach(p => {
    html += `
      <article class="tarjeta">
        <h3>${p.nombre}</h3>
        <p class="categoria">${p.categoria.nombre}</p>
        <p class="precio">$ ${p.precio}</p>
        <p>Stock: ${p.stock}</p>
        <div class="acciones">
          <button onclick="editarProducto(${p.id})">Editar</button>
          <button class="peligro" onclick="eliminarProducto(${p.id})">Eliminar</button>
        </div>
      </article>
    `;
  });

  lista.innerHTML = html;
}

filtroCategoria.addEventListener("change", cargarProductos);

// ---------------------------------------------------------------
// POST y PUT: crear o actualizar (según el formulario tenga un id o no)
// ---------------------------------------------------------------
formulario.addEventListener("submit", function (evento) {
  evento.preventDefault();

  // La categoría se envía como objeto: la API espera { "id": ... }
  const producto = {
    nombre: campoNombre.value,
    precio: Number(campoPrecio.value),
    stock: Number(campoStock.value),
    categoria: { id: Number(campoCategoria.value) }
  };

  let url = URL_API;
  let metodo = "POST";

  if (campoId.value !== "") {
    url = URL_API + "/" + campoId.value;
    metodo = "PUT";
  }

  fetch(url, {
    method: metodo,
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(producto)
  })
    .then(leerRespuesta)
    .then(() => {
      mostrarMensaje("Producto guardado.", false);
      limpiarFormulario();
      cargarProductos();
    })
    .catch(error => mostrarMensaje(error.message, true));
});

// ---------------------------------------------------------------
// DELETE: eliminar producto
// La API responde 204 (sin cuerpo): no se llama a response.json().
// ---------------------------------------------------------------
function eliminarProducto(id) {
  if (!confirm("¿Eliminar el producto " + id + "?")) {
    return;
  }

  fetch(URL_API + "/" + id, { method: "DELETE" })
    .then(leerRespuesta)
    .then(() => {
      mostrarMensaje("Producto eliminado.", false);
      cargarProductos();
    })
    .catch(error => mostrarMensaje(error.message, true));
}

// ---------------------------------------------------------------
// Editar: pasa los datos del producto al formulario
// ---------------------------------------------------------------
function editarProducto(id) {
  const p = productos.find(producto => producto.id === id);

  campoId.value = p.id;
  campoNombre.value = p.nombre;
  campoPrecio.value = p.precio;
  campoStock.value = p.stock;
  campoCategoria.value = p.categoria.id;

  tituloFormulario.textContent = "Editando producto " + p.id;
  botonCancelar.hidden = false;
  window.scrollTo({ top: 0, behavior: "smooth" });
}

function limpiarFormulario() {
  formulario.reset();
  campoId.value = "";
  tituloFormulario.textContent = "Nuevo producto";
  botonCancelar.hidden = true;
}

botonCancelar.addEventListener("click", limpiarFormulario);

// Carga inicial
cargarCategorias();
cargarProductos();
