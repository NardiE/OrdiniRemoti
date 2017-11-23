package com.example.edoardo.ordiniremoti.importazione;


import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Barcode;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Destinazione;
import com.example.edoardo.ordiniremoti.database.Listino;
import com.example.edoardo.ordiniremoti.database.ListinoCliente;
import com.example.edoardo.ordiniremoti.database.TabellaSconto;
import com.example.edoardo.ordiniremoti.database.ScontoC;
import com.example.edoardo.ordiniremoti.database.ScontoCM;
import com.example.edoardo.ordiniremoti.database.ScontoCA;

/**
 * Created by edoardo on 20/12/16.
 */
public class ImportazioneHelper {
    public static int ARTICOLO_DIMENSIONESTRINGA = 159;
    public static int CLIENTE_DIMENSIONESTRINGA = 711;
    public static int DESTINAZIONE_DIMENSIONESTRINGA = 141;
    public static int BARCODE_DIMENSIONESTRINGA = 45;
    public static int LISTINO_DIMENSIONESTRINGA = 198;
    public static int LISTINOCLIENTE_DIMENSIONESTRINGA =171;
    public static int TABELLASCONTO_DIMENSIONESTRINGA =38;
    public static int SCONTOC_DIMENSIONESTRINGA =11;
    public static int SCONTOCM_DIMENSIONESTRINGA =14;
    public static int SCONTOCA_DIMENSIONESTRINGA =26;

    public static Articolo importaArticolo(String line){
        String codice = line.substring(0,15);
        String descrizione = line.substring(15,45);
        String descrizione2 = line.substring(45,75);
        String um = line.substring(75,78);
        String me = line.substring(78,81);     // merceologico
        String in = line.substring(81,84);     //inventario
        String cm = line.substring(84,87);     // categoria merceologica
        String esistenza = line.substring(87,104);
        String pezzi_confezione = line.substring(104,121);
        String lotto_vendita = line.substring(121,137);
        String lotto_riordino = line.substring(137,159);
        Articolo arti = new Articolo(codice, descrizione, descrizione2, um, me,in,cm,esistenza,pezzi_confezione,lotto_vendita,lotto_riordino);
        arti.save();
        return arti;
    }

    public static Cliente importaCliente(String line){
        String codice = line.substring(0,8);
        String descrizione = line.substring(8,38);
        String descrizione2 = line.substring(38,68);
        String listino = line.substring(68,71);
        String telefono = line.substring(74,89);
        String fax = line.substring(89,104);
        String telex = line.substring(104,119);
        String via = line.substring(169,199);
        String cap = line.substring(199,204);
        String citta = line.substring(204,234);
        String provincia = line.substring(234,236);
        String descrizione_blocco = line.substring(236,266);
        String blocco = line.substring(266,267);
        String fuorifido = line.substring(267,268);
        String email = line.substring(268,318);
        String codicepagamento = line.substring(318,321);
        String descrizionepagamento = line.substring(321,381);
        String banca = line.substring(381,481);
        String porto = line.substring(481,511);
        String vettore1 = line.substring(511,611);
        String vettore2 = line.substring(611,711);
        Cliente clie = new Cliente(codice, descrizione, descrizione2, listino, telefono, fax, telex, via, cap, citta, provincia, descrizione_blocco, blocco, fuorifido, email,codicepagamento,descrizionepagamento,banca,vettore1,vettore2);
        clie.save();
        return clie;
    }

    public static Listino importaListino(String line){
        String codicearticolo = line.substring(0,0 + 15);
        String codicelistino = line.substring(15,18);
        Float qt1 = Float.parseFloat(line.substring(18,35));
        Float prezzo1 = Float.parseFloat(line.substring(35,52));
        String sconto1 = line.substring(52,55);
        String provvigioni1 = line.substring(55,63);

        Float qt2 = Float.parseFloat(line.substring(63,80));
        Float prezzo2 = Float.parseFloat(line.substring(80,97));
        String sconto2 = line.substring(97,100);
        String provvigioni2 = line.substring(100,108);

        Float qt3 = Float.parseFloat(line.substring(108,125));
        Float prezzo3 = Float.parseFloat(line.substring(125,142));
        String sconto3 = line.substring(142,145);
        String provvigioni3 = line.substring(145,153);

        Float qt4 = Float.parseFloat(line.substring(153,170));
        Float prezzo4 = Float.parseFloat(line.substring(170,187));
        String sconto4 = line.substring(187,190);
        String provvigioni4 = line.substring(190,198);
        Listino listi = new Listino(codicearticolo, codicelistino,qt1, prezzo1, sconto1, provvigioni1, qt2, prezzo2, sconto2, provvigioni2, qt3, prezzo3, sconto3, provvigioni3, qt4, prezzo4, sconto4, provvigioni4);
        listi.save();
        return listi;
    }

    public static Barcode importaBarcode(String line){
        String codicearticolo = line.substring(0,0 + 15);
        String codiceabarre = line.substring(15,45);
        Barcode barco = new Barcode(codicearticolo, codiceabarre);
        barco.save();
        return barco;
    }
    public static Destinazione importaDestinazione(String line){
        String codicecliente = line.substring(0,0 + 8);
        String codice = line.substring(8,14);
        String descrizione = line.substring(14,44);
        String descrizione2 = line.substring(44,74);
        String via = line.substring(74,104);
        String cap = line.substring(104, 109);
        String citta = line.substring(109, 139);
        String provincia = line.substring(139, 141);
        Destinazione desti = new Destinazione(codicecliente, codice, descrizione, descrizione2, via, cap, citta, provincia);
        desti.save();
        return desti;
    }

    public static ListinoCliente importaListinoCliente(String line) {
        String codicecliente = line.substring(0, 0 + 8);
        String codicearticolo = line.substring(8, 23);
        Float qt1 = Float.parseFloat(line.substring(23, 40));
        Float prezzo1 = Float.parseFloat(line.substring(40, 57));
        String sconto1 = line.substring(57, 60);

        Float qt2 = Float.parseFloat(line.substring(60, 77));
        Float prezzo2 = Float.parseFloat(line.substring(77, 94));
        String sconto2 = line.substring(94, 97);

        Float qt3 = Float.parseFloat(line.substring(97, 114));
        Float prezzo3 = Float.parseFloat(line.substring(114, 131));
        String sconto3 = line.substring(131, 134);

        Float qt4 = Float.parseFloat(line.substring(134, 151));
        Float prezzo4 = Float.parseFloat(line.substring(151, 168));
        String sconto4 = line.substring(168, 171);
        ListinoCliente listic = new ListinoCliente(codicecliente, codicearticolo, qt1, prezzo1, sconto1, qt2, prezzo2, sconto2, qt3, prezzo3, sconto3, qt4, prezzo4, sconto4);
        listic.save();
        return listic;
    }

    public static TabellaSconto importaTabellaSconto(String line) {
        String codice = line.substring(0, 0 + 3);
        Float sconto1 = Float.parseFloat(line.substring(3, 10));
        Float sconto2 = Float.parseFloat(line.substring(10, 17));
        Float sconto3 = Float.parseFloat(line.substring(17, 24));
        Float sconto4 = Float.parseFloat(line.substring(24, 31));
        Float sconto5 = Float.parseFloat(line.substring(31, 38));
        TabellaSconto sco = new TabellaSconto(codice, sconto1, sconto2, sconto3, sconto4, sconto5);
        sco.save();
        return sco;
    }
    public static ScontoC importaScontoC(String line) {
        String codicecliente = line.substring(0, 0 + 8);
        String sconto = line.substring(8, 11);
        ScontoC sco = new ScontoC(codicecliente, sconto);
        sco.save();
        return sco;
    }
    public static ScontoCM importaScontoCM(String line) {
        String codicecliente = line.substring(0, 0 + 8);
        String codicemerceologico = line.substring(8, 11);
        String sconto = line.substring(11, 14);
        ScontoCM sco = new ScontoCM(codicecliente,codicemerceologico, sconto);
        sco.save();
        return sco;
    }
    public static ScontoCA importaScontoCA(String line) {
        String codicecliente = line.substring(0, 0 + 8);
        String codicearticolo = line.substring(8, 23);
        String sconto = line.substring(23, 26);
        ScontoCA sco = new ScontoCA(codicecliente,codicearticolo, sconto);
        sco.save();
        return sco;
    }
}
