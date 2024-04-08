/**
*
* @author irfan eren çiftçi irfan.ciftci1@ogr.sakarya.edu.tr
* @since 07/04/2024
* <p>
* main sınıfı gerekli işlemler ve fonksiyon çağrımları burada yapılıyor
* </p>
*/


package package1;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class test {

	public static void main(String[] args)throws IOException {
Scanner scanner = new Scanner(System.in);
    
        System.out.print("Lütfen geçerli bir URL giriniz: ");
        
        String URL = scanner.nextLine();
        
        String repository ="";
        
        	
        try {
        	// URL'yi kullanarak repository adını oluşturuyoruz.(split yöntemiyle)
            String[] parts = URL.split("/");
            repository = parts[parts.length - 1].replace(".git", "");

            // processbuilder ile git klonlama işlemini yapıyoruz.
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("git", "clone", URL, repository);
            processBuilder.directory(new File(System.getProperty("user.dir")));
           
            Process process = processBuilder.start();

            // klonlanma işleminin gerçekleşiğ gerçekleşmediğini kontrol ediyoruz.
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Repository klonlandı.");
                
            } else {
                System.out.println("Repository klonlanamadı!!!!");
            }
        } catch (IOException | InterruptedException e) { // hatayı yakalıyoruz!!
            e.printStackTrace();
            System.out.println("Repository klonlanırken bir hata oluştu: " + e.getMessage());
        }
        
    
        //klonlanan repository'i file yapısında tutuyoruz.
        
        File klonlananRepo = new File(repository);

        // klonlanan repository'deki *.java uzantılı dosyaların tümünü bir listeye atıyoruz.
        
        List<File> tumJava = new ArrayList<>();
        Dosya.javaListesi(klonlananRepo, tumJava);
        
        //tüm java dosyalarının bulunduğu listeden içinde sadece sınıf olanları ayrı bir diziye atıyoruz.
        
        List<File> sinifJava = new ArrayList<>();
        Dosya.sinifliJavaListesi(tumJava, sinifJava);

        //istenilen ekran çıktılarını burada yazdırıyoruz
        
        for (File sinif : sinifJava) { //for-each döngüsü ile sinif içeren java dosyalarında geziniyoruz.
        	
            System.out.println("Sınıf: " + sinif.getName()); // sinifin adını getName() metodu ile yazdırıyoruz.
            //dosyayolu diye bir string ifadesi oluşturuyoruz. bu ifade fonksiyonları çağırırken hangi dizine gideceğini gösterecek
            String dosyayolu = sinif.getAbsolutePath();
            
            //fonksiyonalrı burada çağırıyourz.
            
            int fonksiyon = Dosya.fonksiyonSayisi(dosyayolu);
            int yorum = Dosya.yorumSayisi(dosyayolu);
            int javadoc = Dosya.javadocSayisi(dosyayolu);
            int kodsatir = Dosya.kodSatirSayisi(dosyayolu);
            int toplamsatir = Dosya.toplamSatirSayisi(dosyayolu);
            
            //istenilen yüzde hesabı için değişkenleri doube cinsinden işlemlere tabi tutuyrouz.
            
            double doublejavadoc = (double) javadoc;
            double doublefonksiyon = (double) fonksiyon;
            double doubleyorum = (double) yorum;
            double doublekodsatir = (double) kodsatir;
            double yG = ((doublejavadoc+doubleyorum)*0.8)/doublefonksiyon;
            double yH = (doublekodsatir/doublefonksiyon)*0.3;
            double yorumSapmaYuzdesi = ((100*yG)/yH)-100;
            
            // yazdırma işlemlerini burada yapıyoruz.
            
            System.out.println("Javadoc Satır Sayısı:" + javadoc);
            System.out.println("Yorum Satır Sayısı:" + yorum);
            System.out.println("Kod Satır Sayısı:" + kodsatir);
            System.out.println("LOC:" + toplamsatir);
            System.out.println("Fonksiyon Sayısı:" + fonksiyon);
            System.out.println("Yorum Sapma Yüzdesi:" + yorumSapmaYuzdesi);
            System.out.println("--------------------------");
            
            
        }
        
      //işlemler bittikten sonra silme işlemini yapıyoruz. yoksa program bir daha çalıştığında klonlanan repository dizinde bulunur ve tekrar klonlama yapılamaz.
        
        if (klonlananRepo.exists() && klonlananRepo.isDirectory()) {
        
        Dosya.sil(klonlananRepo);
        System.out.println("Klonlanan repository silindi.");
    } else {
        System.out.println("Klonlanan repository bulunamadı");
    }
	
        
        scanner.close();
		
	}

}
