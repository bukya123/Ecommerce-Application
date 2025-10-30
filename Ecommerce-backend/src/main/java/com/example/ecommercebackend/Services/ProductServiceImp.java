package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ControllerAdivce.APIException;
import com.example.ecommercebackend.ControllerAdivce.ResourceNotFoundException;
import com.example.ecommercebackend.DTOs.CartRequestDTO;
import com.example.ecommercebackend.DTOs.ProductRequestDTO;
import com.example.ecommercebackend.DTOs.ProductResponseDTO;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Cart;
import com.example.ecommercebackend.Modules.Category;
import com.example.ecommercebackend.Modules.Product;
import com.example.ecommercebackend.Repositories.CartRepo;
import com.example.ecommercebackend.Repositories.CategoryRepo;
import com.example.ecommercebackend.Repositories.ProductRepo;
import com.example.ecommercebackend.Util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CartRepo cartRepo;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Autowired
    private CartService cartServiceImp;

    @Autowired
    private AuthUtil authUtil;


    @Override
    public ProductRequestDTO addProduct(ProductRequestDTO productRequestDTO, Long categoryId){

        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
        if(optionalCategory.isPresent()==false){
            throw new ResourceNotFoundException("Category","categoryId",categoryId);
        }
        List<Product> products=optionalCategory.get().getProducts();
        for(Product p:products){
            if(p.getProductName().equals(productRequestDTO.getProductName())){
                throw new APIException("Product already exists");
            }
        }
        Product product = modelMapper.map(productRequestDTO, Product.class);
        Category category = optionalCategory.get();
        product.setCategory(category);
        product.setImage("Default.png");
        product.setAppUser(authUtil.loggedInUser());
        double sp=product.getPrice()-((product.getDiscount()*0.01)*product.getPrice());
        product.setSpecialPrice(sp);
        productRepo.save(product);
        return modelMapper.map(product, ProductRequestDTO.class);
    }

    @Override
    public ProductResponseDTO getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Specification<Product> spec = Specification.unrestricted();
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
        }
//
        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("category").get("categoryName"), category));
        }
        Page<Product> productPage = productRepo.findAll(spec,pageDetails);
        List<Product> products=productPage.getContent();

        

        List<ProductRequestDTO> productRequestDTOS=products.stream()
                .map(product -> {
                    ProductRequestDTO productDTO = modelMapper.map(product, ProductRequestDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        productResponseDTO.setPageSize(productPage.getSize());
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setLastPage(productPage.isLast());


        return productResponseDTO;
    }

//    @Override
//    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {
//        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
//                ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//
//        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
//        Specification<Product> spec = Specification.unrestricted();
//        if (keyword != null && !keyword.isEmpty()) {
//            spec = spec.and((root, query, criteriaBuilder) ->
//                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
//        }
//
//        if (category != null && !category.isEmpty()) {
//            spec = spec.and((root, query, criteriaBuilder) ->
//                    criteriaBuilder.like(root.get("category").get("categoryName"), category));
//        }
//
//        Page<Product> pageProducts = productRepo.findAll(spec, pageDetails);
//
//        List<Product> products = pageProducts.getContent();
//
//        List<ProductRequestDTO> productDTOS = products.stream()
//                .map(product -> {
//                    ProductRequestDTO productDTO = modelMapper.map(product, ProductRequestDTO.class);
//                    productDTO.setImage(constructImageUrl(product.getImage()));
//                    return productDTO;
//                })
//                .toList();
//
//        ProductResponseDTO productResponse = new ProductResponseDTO();
//        productResponse.setContent(productDTOS);
//        productResponse.setPageNumber(pageProducts.getNumber());
//        productResponse.setPageSize(pageProducts.getSize());
//        productResponse.setTotalElements(pageProducts.getTotalElements());
//        productResponse.setTotalPages(pageProducts.getTotalPages());
//        productResponse.setLastPage(pageProducts.isLast());
//        return productResponse;
//    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
    }


    @Override
    public ProductResponseDTO getAllProductsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductRequestDTO> productDTOS = products.stream()
                .map(product -> {
                    ProductRequestDTO productDTO = modelMapper.map(product, ProductRequestDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();

        ProductResponseDTO productResponse = new ProductResponseDTO();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponseDTO getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        AppUser user = authUtil.loggedInUser();
        Page<Product> pageProducts = productRepo.findByAppUser(user, pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductRequestDTO> productDTOS = products.stream()
                .map(product -> {
                    ProductRequestDTO productDTO = modelMapper.map(product, ProductRequestDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();

        ProductResponseDTO productResponse = new ProductResponseDTO();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public  ProductResponseDTO getProductsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String  sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Optional<Category> category= categoryRepo.findById(categoryId);
        if(!category.isPresent()){
            throw new ResourceNotFoundException("Category","categoryId",categoryId);
        }
        Pageable pageDetails=PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepo.findByCategoryOrderByPrice(category.get(),pageDetails);
        List<Product> products=productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("No Products exist with "+categoryId);
        }

        List<ProductRequestDTO> productRequestDTOS=products.stream().map(product -> {
                    ProductRequestDTO productDTO = modelMapper.map(product, ProductRequestDTO.class);
                    return productDTO;
                })
                .toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        productResponseDTO.setPageSize(productPage.getSize());
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String  sortOrder) {
        Sort sortByAndOder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails=PageRequest.of(pageNumber,pageSize,sortByAndOder);
        Page<Product> productspage=productRepo.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);
        List<Product> products=productspage.getContent();
        if(products.isEmpty()){
            throw new APIException("Products not found with keyword "+keyword);
        }
        // if u even type sp for sports=> it gets data
        List<ProductRequestDTO> productRequestDTOS=products.stream().map(product -> {
                    ProductRequestDTO productDTO = modelMapper.map(product, ProductRequestDTO.class);
                    return productDTO;
                })
                .toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        productResponseDTO.setPageSize(productspage.getSize());
        productResponseDTO.setPageNumber(productspage.getNumber());
        productResponseDTO.setTotalPages(productspage.getTotalPages());
        productResponseDTO.setTotalElements(productspage.getTotalElements());
        productResponseDTO.setLastPage(productspage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductRequestDTO updateProducts(Long  productId,ProductRequestDTO productRequestDTO){
        //first get the product with productId
        Product product = modelMapper.map(productRequestDTO, Product.class);
        Optional<Product> productOptional = productRepo.findById(productId);
        if(!productOptional.isPresent()){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        Product product1 = productOptional.get();
        product1.setPrice(product.getPrice());
        product1.setDiscount(product.getDiscount());
        product1.setImage(product.getImage());
        product1.setSpecialPrice(product.getSpecialPrice());
        product1.setProductName(product.getProductName());
        product1.setProductDescription(product.getProductDescription());
        product1.setQuantity(product.getQuantity());
        productRepo.save(product1);
        //
         //update product in the cart as well .if the product is present in cart
        // first get all the carts in which this particular product is present
        List<Cart> carts= cartRepo.findByProductId(productId);
        List<CartRequestDTO> cartDTOs = carts.stream().map(cart -> {
            CartRequestDTO cartDTO = modelMapper.map(cart, CartRequestDTO.class);

            List<ProductRequestDTO> products = cart.getCartItem().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductRequestDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartServiceImp.updateProductInCarts(cart.getCartId(), productId));
        return modelMapper.map(product1, ProductRequestDTO.class);
    }

    @Override
    public ProductRequestDTO deleteProduct(Long  productId){
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if(!optionalProduct.isPresent()){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        Product product = optionalProduct.get();


            // delete product if it is in cart as well
        List<Cart> carts=cartRepo.findByProductId(productId);
        carts.forEach(cart -> cartServiceImp.deleteProductFromCart(cart.getCartId(), productId));

        productRepo.deleteById(productId);
        return modelMapper.map(product, ProductRequestDTO.class);

    }

    @Override
    public ProductRequestDTO updateProductImage(Long productId, MultipartFile productImage) throws IOException {
        Product productFromDb = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));


                // upload image to server
                // get the file name of uploaded image
//        String path="image/";
//        hardcoding the path in each -> tight coupling.Instead store it in application.properties
        String fileName = fileService.uploadImage(path, productImage);

        // upload new file name to the product
        productFromDb.setImage(fileName);

        Product updatedProduct = productRepo.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductRequestDTO.class);
    }
}
