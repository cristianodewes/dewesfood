package dewes.antonio.cristiano.dewesfood.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dewes.antonio.cristiano.dewesfood.domain.cliente.Cliente;
import dewes.antonio.cristiano.dewesfood.domain.cliente.ClienteRepository;
import dewes.antonio.cristiano.dewesfood.domain.restaurante.Restaurante;
import dewes.antonio.cristiano.dewesfood.domain.restaurante.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional
	public void saveRestaurante(Restaurante restaurante) throws ValidationException {

		if (!validateEmail(restaurante.getEmail(), restaurante.getId())) {
			throw new ValidationException("O e-mail est� duplicado!");
		}

		if( restaurante.getId() != null) {
			Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow();			
			restaurante.setSenha(restauranteDB.getSenha());
			
		} else {
			restaurante.encryptPassword();			
			restaurante = restauranteRepository.save(restaurante);
			restaurante.setLogotipoFileName();
			imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
			
		}
		restauranteRepository.save(restaurante);
	}
	
	private boolean validateEmail(String email, Integer id) {
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente != null) {
			return false;
		}
		
		Restaurante restaurante = restauranteRepository.findByEmail(email);
		
		if(restaurante != null) {
			if(id == null) {
				return false;
			} 
			
			if( !restaurante.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
}
