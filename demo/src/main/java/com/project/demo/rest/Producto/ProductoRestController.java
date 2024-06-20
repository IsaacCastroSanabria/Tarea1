package com.project.demo.rest.Producto;

import com.project.demo.logic.entity.Producto.Producto;
import com.project.demo.logic.entity.Producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {

    @Autowired
    private ProductoRepository ProductoRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN_ROLE')")
    public List<Producto> getAllProductos() {
        return ProductoRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Producto addCategoria(@RequestBody Producto producto) {
        return ProductoRepository.save(producto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN_ROLE')")
    public Producto getProductoById(@PathVariable Integer id) {
        return ProductoRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Producto updateProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        return ProductoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setNombre(producto.getNombre());
                    existingProducto.setDescription(producto.getDescription());
                    existingProducto.setPrecio(producto.getPrecio());
                    existingProducto.setCantStock(producto.getCantStock());
                    existingProducto.setCategoria(producto.getCategoria());
                    return ProductoRepository.save(existingProducto);
                })
                .orElseGet(() -> {
                    producto.setId(id);
                    return ProductoRepository.save(producto);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Integer id) {
        ProductoRepository.deleteById(id);
    }

}