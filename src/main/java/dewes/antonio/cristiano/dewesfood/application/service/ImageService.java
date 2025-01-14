package dewes.antonio.cristiano.dewesfood.application.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dewes.antonio.cristiano.dewesfood.util.IOUtils;

@Service
public class ImageService {

	@Value("${dewesfood.files.logotipos}")
	private String logotiposDir;

	@Value("${dewesfood.files.comidas}")
	private String comidasDir;

	@Value("${dewesfood.files.categorias}")
	private String categoriasDir;

	public void uploadLogotipo(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, logotiposDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}

	public byte[] getBytes(String type, String imgName) {
		
		try {
			String dir;
	
			if ("comida".equals(type)) {
				dir = comidasDir;
			} else if ("logotipo".equals(type)) {
				dir = logotiposDir;
			} else if ("categoria".equals(type)) {
				dir = categoriasDir;
			} else {
				throw new Exception(type + " n�o � um tipo de imagem inv�lida");
			}
			
			return IOUtils.getBytes(Paths.get(dir, imgName));
		} catch ( Exception e) {
			throw new ApplicationServiceException(e);
		}
	}

}
