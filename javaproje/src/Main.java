import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static Timer zaman; // Ekran süresi için zamanlayıcı
    private static JLabel calismasuresietiketi; // Toplam çalışma süresi etiketi
    private static JLabel gerisayimetiketi; // Kalan süre etiketi
    private static JLabel altyazietiketi; // Alt başlık etiketi (motivasyonel mesajlar)
    private static int toplamCalismaSuresi; // Kullanıcının belirlediği toplam çalışma süresi (dakika cinsinden)
    private static int molaSuresi; // Mevcut mola süresi
    private static int kalanCalismaSuresi; // Kalan çalışma süresi
    private static boolean molaZorunlu = false; // Zorunlu mola kontrolü
    private static String[] mesajlar = {
            "Çalışma sürenizi takip edin ve ara vermeyi unutmayın!",
            "Sağlığınız için düzenli aralar verin.",
            "Gözlerinizi dinlendirmek için ekrandan uzaklaşın.",
            "Verimli çalışmak için kısa molalar verin."
    };
    private static int messageIndex = 0; // Mesaj dizisindeki mevcut indeks
    private static Timer messageTimer; // Mesajları değiştiren zamanlayıcı

    public static void main(String[] args) {
        // Ana pencereyi oluşturma
        JFrame frame = new JFrame("");
        frame.setSize(500, 400); // Pencerenin boyutunu belirledim
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pencere kapatıldığında programın sonlanmasını sağladım
        frame.setLayout(new BorderLayout()); // Pencereye BorderLayout düzenini uyguladım

        // Başlık paneli
        JPanel titlePanel = new JPanel(); // Başlık paneli oluşturdum ve
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Ekran Süresi Takibi", SwingConstants.CENTER); // Başlık etiketini oluşturdum ve ortaladım
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Başlık etiketinin fontunu ayarladım
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        // Alt başlık etiketi (motivasyonel mesaj)
        altyazietiketi = new JLabel(mesajlar[messageIndex], SwingConstants.CENTER); // Motivasyon mesaj etiketini oluşturdum ve başlık paneline ekledim
        altyazietiketi.setFont(new Font("Serif", Font.PLAIN, 16)); // Motivasyon etiketinin fontunu ayarladım
        altyazietiketi.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(altyazietiketi);

        // Başlık panelini ana pencereye ekleme
        frame.add(titlePanel, BorderLayout.NORTH); // Başlık panelini ana pencerenin kuzeyine ekledim

        // Merkezi panel
        JPanel centerPanel = new JPanel(); // Merkezi paneli oluşturdum ve dikey olarak kutu düzeninde uyguladım
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Çalışma süresi etiketi
        calismasuresietiketi = new JLabel("Toplam çalışma süresi: 0 dakika", SwingConstants.CENTER); // Toplam çalışma süresi etiketini oluşturup merkezi panele ekledim
        calismasuresietiketi.setFont(new Font("Serif", Font.PLAIN, 18));
        calismasuresietiketi.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(calismasuresietiketi); // Merkezi panelimizi ana sayfanın ortasına ekledim

        // Geri sayım etiketi
        gerisayimetiketi = new JLabel("Kalan süre: ", SwingConstants.CENTER); // Kalan süre etiketini oluşturdum ve merkezi panele ekledim
        gerisayimetiketi.setFont(new Font("Serif", Font.PLAIN, 18)); // Özelliklerini ayarladım (font, yazı tipi vs.)
        gerisayimetiketi.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(gerisayimetiketi);

        // Merkezi paneli ana pencereye ekleme
        frame.add(centerPanel, BorderLayout.CENTER); // Merkezi paneli ana pencerenin ortasına ekledim

        // Alt panel
        JPanel bottomPanel = new JPanel(); // Alt paneli oluşturdum ve FlowLayout düzeninde ayarladım
        bottomPanel.setLayout(new FlowLayout());

        // Süre giriş alanı ve buton
        JTextField calismaInput = new JTextField(5); // Kullanıcı giriş alanı
        JButton setCalismaButton = new JButton("Toplam Çalışma Süresini Ayarla (dk)"); // Çalışma süresini ayarlayan buton
        setCalismaButton.addActionListener(new SetCalismaListener(calismaInput, frame));
        bottomPanel.add(calismaInput); // Kullanıcının toplam çalışma süresini girebileceği metin alanını oluşturdum
        bottomPanel.add(setCalismaButton); // Çalışma süresini belirleme butonu

        // Alt paneli ana pencereye ekleme
        frame.add(bottomPanel, BorderLayout.SOUTH); // Alt paneli ana pencerenin güneyine ekledim

        // Mesaj zamanlayıcısını başlat
        startMessageTimer();

        // Pencereyi görünür hale getirme
        frame.setVisible(true);
    }

    // Zamanlayıcıyı belirli bir dakika değeri ile ayarlayan method
    private static void setTimer(int calismaDakika, JFrame frame) {
        if (zaman != null) {
            zaman.stop();
        }
        toplamCalismaSuresi = calismaDakika; // Toplam çalışma süresini ayarla
        molaSuresi = (int) (toplamCalismaSuresi * 0.2); // Toplam sürenin %20'si kadar mola süresi ayarla
        kalanCalismaSuresi = toplamCalismaSuresi - molaSuresi; // Mola süresini toplam çalışma süresinden çıkar
        gerisayimetiketi.setText("Kalan süre: " + kalanCalismaSuresi + " dakika"); // Geri sayım etiketini güncelle
        zaman = new Timer(1000, new TimerListener(frame)); // Yeni zamanlayıcı oluştur
        zaman.start();
    }

    // Mesajları belirli aralıklarla değiştiren zamanlayıcıyı başlatan method
    private static void startMessageTimer() {
        messageTimer = new Timer(10000, new ActionListener() { // 10 saniyede bir mesaj değişir
            
            public void actionPerformed(ActionEvent e) {
                messageIndex = (messageIndex + 1) % mesajlar.length; // Mesaj dizisindeki bir sonraki mesaja geç
                altyazietiketi.setText(mesajlar[messageIndex]); // Alt başlık etiketini güncelle
            }
        });
        messageTimer.start();
    }

    // Çalışma süresini ayarlayan buton için dinleyici
    static class SetCalismaListener implements ActionListener {
        private final JTextField calismaInput; // Kullanıcıdan çalışma süresi girişini alır
        private final JFrame frame;

        public SetCalismaListener(JTextField calismaInput, JFrame frame) {
            this.calismaInput = calismaInput;
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                int minutes = Integer.parseInt(calismaInput.getText()); // Kullanıcı girişini dakika olarak al
                if (minutes < 45) {
                    JOptionPane.showMessageDialog(frame, "Lütfen en az 45 dakika girin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı göster
                    return;
                } else if (minutes > 480) {
                    JOptionPane.showMessageDialog(frame, "Lütfen en fazla 480 dakika girin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı göster
                    return;
                }
                setTimer(minutes, frame); // Geçerli bir dakika değeri girildiğinde zamanlayıcıyı ayarla
                JOptionPane.showMessageDialog(frame, "Toplam çalışma süresi " + minutes + " dakika olarak ayarlandı.", "Bilgi", JOptionPane.INFORMATION_MESSAGE); // Bilgilendirme mesajı göster
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir dakika değeri girin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı göster
            }
        }
    }

    // Zamanlayıcıyı yöneten ve geri sayımı güncelleyen dinleyici
    static class TimerListener implements ActionListener {
        private final JFrame frame;
        private int gecensaniye = 0; // Geçen saniye sayısı
        private int toplamgecensaniyesayisi = 0;

        public TimerListener(JFrame frame) { // sınıfın yapıcısıdır
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) { //Bu metod, zamanlayıcı tetiklendiğinde çalışır ve geri sayımı günceller.
            gecensaniye++;
            toplamgecensaniyesayisi++;

            if (molaZorunlu) {
                int kalanmolasaniyesi = molaSuresi * 60 - gecensaniye; // Kalan mola saniye sayısını hesapla
                int kalanmoladakikasi = kalanmolasaniyesi / 60; // Kalan mola dakika sayısını hesapla
                gerisayimetiketi.setText(String.format("Mola: %d dakika kaldı", kalanmoladakikasi)); // Mola geri sayım etiketini güncelle

                if (kalanmolasaniyesi <= 0) {
                    molaZorunlu = false; // Mola bitti
                    gecensaniye = 0; // Çalışma süresi için zamanlayıcıyı sıfırla
                    toplamgecensaniyesayisi = 0; // Toplam geçen saniyeleri sıfırla
                    gerisayimetiketi.setText("Kalan süre: " + (kalanCalismaSuresi - (toplamgecensaniyesayisi / 60)) + " dakika"); // Çalışma geri sayım etiketini güncelle
                }
            } else {
                if (gecensaniye % 60 == 0) {
                    calismasuresietiketi.setText("Toplam çalışma süresi: " + (toplamgecensaniyesayisi / 60) + " dakika"); // Çalışma süresi etiketini güncelle
                }

                int remainingMinutes = kalanCalismaSuresi - (toplamgecensaniyesayisi / 60); // Kalan dakika sayısını hesapla
                gerisayimetiketi.setText(String.format("Kalan süre: %d dakika", remainingMinutes)); // Geri sayım etiketini güncelle

                if (remainingMinutes <= 0) {
                    if (toplamgecensaniyesayisi / 60 < toplamCalismaSuresi) {
                        // Mola zamanı
                        molaZorunlu = true;
                        gecensaniye = 0; // Molayı başlatmak için zamanlayıcıyı sıfırla
                        JOptionPane.showMessageDialog(frame, "Mola zamanı!", "Hatırlatma", JOptionPane.WARNING_MESSAGE); // Mola hatırlatma mesajı göster
                    } else {
                        // Toplam çalışma süresi doldu
                        zaman.stop(); // Zamanlayıcıyı durdur
                        JOptionPane.showMessageDialog(frame, "Çalışma süreniz tamamlandı!", "Bilgi", JOptionPane.INFORMATION_MESSAGE); // Çalışma süresi tamamlandı mesajı göster
                    }
                }
            }
        }
    }
}

