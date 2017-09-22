package br.gov.ce.tce.srh.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.service.ParametroService;

/**
 * Classe para pegar a imagem no servidor
 * 
 * @author robstown
 *
 */
public class GetArquivoServlet extends HttpServlet {


	/**
	 *
	 */
	private static final long serialVersionUID = 1639755957415573976L;


	@SuppressWarnings("resource")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String arquivo = request.getParameter("arquivo");

		response.setHeader("Content-disposition",
                ";filename=" + arquivo +
                ";creation-date=" + new Date() );

		response.setContentType(new MimetypesFileTypeMap().getContentType(arquivo));
        ServletOutputStream outStream = response.getOutputStream();

        // pegando o caminho do arquivo no servidor
        ParametroService parametroService = (ParametroService) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("parametroService");
        Parametro parametro = parametroService.getByNome("pathImageSRH");
        
        if (parametro == null)
        	throw new ServletException("Imagem nao encontrada");

        // pegando o arquivo
        InputStream in = new FileInputStream(parametro.getValor() + arquivo);

        if(request.getParameter("resize")!=null&&request.getParameter("resize").equals("S")){
        	int w = 60;
        	int h = 60;
        	try {
        		if(request.getParameter("w")!=null)
        		{
        			w = Integer.parseInt(request.getParameter("w"));
        		}
        		if(request.getParameter("h")!=null)
        		{
        			h = Integer.parseInt(request.getParameter("h"));
        		}
				in = scaleImage(in,w,h,false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        byte[] buffer = new byte[1024];
        int n = 0;
        while ( ( n = in.read(buffer)) != -1) {
            outStream.write(buffer, 0, n);
            outStream.flush();
        }
        in.close();
        outStream.close();
	}

	public static InputStream scaleImage(InputStream p_image, int p_width, int p_height, boolean proporcional) throws Exception {

	     InputStream imageStream = new BufferedInputStream(p_image);
	     Image image = (Image) ImageIO.read(imageStream);

	     int thumbWidth = p_width;
	        int thumbHeight = p_height;

	        // Make sure the aspect ratio is maintained, so the image is not skewed
	        double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	        int imageWidth = image.getWidth(null);
	        int imageHeight = image.getHeight(null);
	        double imageRatio = (double)imageWidth / (double)imageHeight;

	        if (proporcional) {
		        if (thumbRatio < imageRatio) {
		          thumbHeight = (int)(thumbWidth / imageRatio);
		        } else {
		          thumbWidth = (int)(thumbHeight * imageRatio);
		        }

	        } else {
	        	thumbWidth = p_width;
	        	thumbHeight = p_height;
	        }

	        // Draw the scaled image
	        BufferedImage thumbImage = new BufferedImage(thumbWidth,
	          thumbHeight, BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphics2D = thumbImage.createGraphics();
	        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

	        // Write the scaled image to the outputstream
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	        JPEGEncodeParam param = encoder.
	          getDefaultJPEGEncodeParam(thumbImage);
	        int quality = 100; // Use between 1 and 100, with 100 being highest quality
	        quality = Math.max(0, Math.min(quality, 100));
	        param.setQuality((float)quality / 100.0f, false);
	        encoder.setJPEGEncodeParam(param);
	        encoder.encode(thumbImage);
	        ImageIO.write(thumbImage, "" , out);

	        // Read the outputstream into the inputstream for the return value
	        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

	        return bis;
	    }

}
