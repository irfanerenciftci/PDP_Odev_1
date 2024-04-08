/**
*
* @author irfan eren çiftçi irfan.ciftci1@ogr.sakarya.edu.tr
* @since 07/04/2024
* <p>
* gerekli olan fonksiyon tanımlamaları burada yapılır.
* </p>
*/


package package1;
import java.io.File;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


//silme fonksiyonu

public class Dosya {
	public static void sil(File dizin) {
		
		
        File[] dosyalar = dizin.listFiles();
        
        if (dosyalar != null) {
        	
            for (File dosya : dosyalar) {
                sil(dosya);
                
            }
        }
        
        dizin.delete();
    }

//klonlana repository'deki *.java dosyalarını listeye atama fonksiyonu.
	
	public static void javaListesi(File dizin, List<File> javaliste) {
		File[] dosya = dizin.listFiles(); // dosya adında bir dizi oluşturuyoruz ve tüm klonlanan repo'daki tüm dosyalrı buna atıyoruz.
		if (dosya != null) {
			for (File javadosyasi : dosya) { // for-each döngüsü ile dosya dizisindeki tüm dosyaları geziyoruz.
				if (javadosyasi.isFile() && javadosyasi.getName().endsWith(".java")) { // eğer .java ile biten bir dosya varsa onu javaliste adındaki yeni oluşturduğumuz listeye atıyoruz.
					javaliste.add(javadosyasi);
				} else if (javadosyasi.isDirectory()) {
					javaListesi(javadosyasi, javaliste);
            }
        }
    }
  }
	
	
	// *.java dosyalarından içinde sınıf olanları ayıklama fonksiyonu
	
	public static void sinifliJavaListesi(List<File> javaliste, List<File> sinifliste) {
        for (File java : javaliste) { // for-each döngüsü ile javalistedeki tüm dosyaları tek tek geziyoruz.
        	
            try (BufferedReader reader = new BufferedReader(new FileReader(java))) {
                String satir;
                /* sınıfvarmi adinda boolean cinsinden bir değişken oluşturuyoruz 
                 * while döngüsünde herbirini gezip "class " içeriyorsa sinifliste adındaki listeye atıyoruz
                 * */
                boolean sinifvarmi = false;
                
                while ((satir = reader.readLine()) != null) {
                    if (satir.contains("class ")) {
                        sinifvarmi = true;
                        break;
                    }
                }
                if (sinifvarmi) {
                    sinifliste.add(java);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	//fonksiyon sayısı bulma fonksiyonu
	
	public static int fonksiyonSayisi(String dosyaYolu) throws IOException {
        int fonksiyonSayisi = 0;
        BufferedReader reader = new BufferedReader(new FileReader(dosyaYolu)); // bufferedreader metoduyla verilen dizindeki dosyayı satır satır geziyoruz.
        String satir;

        while ((satir = reader.readLine()) != null) { // trim() fonksiyonu ile satırın başındaki ve sonundaki boşluklar kaldırılır.
            if (satir.trim().startsWith("public") && satir.trim().contains("(")) { // eğer "public" ile başlayıp "(" içeriyorsa döndüreceğimiz değişken bir artırılıyor.
                fonksiyonSayisi++;
            }
        }

        reader.close();
        return fonksiyonSayisi;
    }
	
	//yorum satır sayısını bulma fonksiyonu.
	
	public static int yorumSayisi(String dosyaYolu) throws IOException {
		
		int yorumSatirSayisi = 0;
        BufferedReader reader = new BufferedReader(new FileReader(dosyaYolu)); // bufferedreader metoduyla verilen dizindeki dosyayı satır satır geziyoruz.
        String satir;

        while ((satir = reader.readLine()) != null) { // trim() fonksiyonu ile satırın başındaki ve sonundaki boşluklar kaldırılır.
            // tek satirlik yorumlar için
            if (satir.trim().contains("//")) {
                yorumSatirSayisi++;
            }
            
            
            //çoklu satirli yorumlar için
            if (satir.trim().startsWith("/*")&& !satir.trim().startsWith("/**") && !satir.trim().endsWith("*/")) { // "/**" ifadesi javadoc sayıldığı için bu ifadeyi içermemesi koşulu vardır
            	while ((satir = reader.readLine())!=null) {
            		if (satir.trim().endsWith("*/")) {
                              break;
                          }
                           yorumSatirSayisi++;
            		}
            	}
            
            
            }
        reader.close();
        return yorumSatirSayisi;
        }
	
//javadoc satır sayısını bulma fonksiyonu
	
	public static int javadocSayisi(String dosyaYolu) throws IOException{
		
		int javadocSatirSayisi=0;
		BufferedReader reader = new BufferedReader(new FileReader(dosyaYolu)); // bufferedreader metoduyla verilen dizindeki dosyayı satır satır geziyoruz.
        String satir;
        
        while((satir = reader.readLine()) !=null) {
        	if(satir.trim().startsWith("/**") && !satir.trim().endsWith("*/")) { // yorum satır sayısından farklı olarak "/**" ifadesi ile başlaması koşulu vardır
        		while ((satir = reader.readLine())!=null) {
            		if (satir.trim().endsWith("*/")) { // satır sonunda "*/" varsa javadoc ifadesi bitmiş olup döngüden çıkar.
                              break;
                          }
                           javadocSatirSayisi++;
            		}
        	}
        }
		
		reader.close();
		return javadocSatirSayisi;
		
		
	}
	
	//yorum ve boşluk haricindeki satır sayısı bulma fonksiyonu
	
	public static int kodSatirSayisi(String dosyaYolu) throws IOException{
		
		
		int kodSatir =0;
		BufferedReader reader = new BufferedReader(new FileReader(dosyaYolu)); // bufferedreader metoduyla verilen dizindeki dosyayı satır satır geziyoruz.
        String satir;
        
        while((satir = reader.readLine()) !=null) {
        	satir =satir.trim(); // trim() fonksiyonu ile satırın başındaki ve sonundaki boşluklar kaldırılır.
        	
        	// sırasıyla boşluk içermeyen, "//", "/*", "*" ifadeleri de yorum satırı sayıldığından bunları da içeremez koşulları vardır
        	
        	if (!satir.isEmpty() && !satir.startsWith("//") && !satir.startsWith("/*")&& !satir.startsWith("*")) { 
                kodSatir++;
            }
        	
        }
        reader.close();
        return kodSatir;
		
		
	}	
	
	// toplam satır sayısını bulma fonksiyonu
	
	public static int toplamSatirSayisi(String dosyaYolu) throws IOException{
		
		int satirSayisi=0;
		BufferedReader reader = new BufferedReader(new FileReader(dosyaYolu)); // bufferedreader metoduyla verilen dizindeki dosyayı satır satır geziyoruz.
        
        // dosya sonuna gelene kada yani null olana kadar her satırı gezer ve while döngüsü sayesinde her defasında satirSayisi bir artirilir.
        while(reader.readLine()!=null) {
        	satirSayisi++;
        }
        reader.close();
        return satirSayisi;
	}
}
	
	
	
	
	
	
	
	

	
	


