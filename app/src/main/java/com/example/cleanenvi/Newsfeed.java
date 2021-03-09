package com.example.cleanenvi;

import java.io.IOException;
import org.json.JSONException;
import com.example.cleanenvi.helpers.ResponseManager;

public class Newsfeed {

    public int newsRowNumber;
    public String[] singleNewsArray;
    public String[][] NewsInString = {
            {"Das gehört in die Papiertonne:", "-\tPapier, Pappe, Kartons\n" +
                    "-\tz.B. Zeitungen, Bücher, Magazine\n" +
                    "\n" +
                    "Das darf NICHT in die Papiertonne:\n" +
                    "\n" +
                    "-\tKüchentücher, Servietten, Taschentücher, Kosmetiktücher\n" +
                    "-\tFotos\n" +
                    "-\tWachs- und Ölpapiere\n" +
                    "-\tTrägerpapier von Aufklebern\n" +
                    "-\tVerschmutztes Papier/Verpackungen \n" +
                    "-\tBackpapier\n" +
                    "-\tGetränkekartons \n"},
            {"Das gehört in den gelben Sack:", "-\tVerpackungen aus Plastik, Metallen, Naturmaterialien\n" +
                    "-\tPlastikverpackungen \n" +
                    "-\tz.B. Spülmittelflasche, Shampoos, Lebensmittelverpackungen \n" +
                    "-\tAlufolie\n" +
                    "-\tKonservendosen\n" +
                    "-\tKronkorken\n" +
                    "-\tPlastiktüten\n" +
                    "-\tStyropor\n" +
                    "-\tAlles mit „grünem Punkt“\n"},
            {"Das darf NICHT in den gelben Sack:","-\tGlas, Papier\n" +
                    "-\tEinwegrasierer, Zahnbürsten, Kleiderbügel, Feuerzeug, Eimer, Spielzeug, Keramik\n" +
                    "-\tCDs, Videokassetten \t\n" +
                    "-\tToilettenbürsten, Windeln\n" +
                    "-\tDosen etc. die unter Druck stehen außer Sprühsahne\n" +
                    "-\tz.B. Deospray, \n" +
                    "-\tgiftige oder Aerosol enthaltende Verpackungen\n" +
                    "-\tz.B. Reinigungsmittel, Farbe, Lacke, Textilreiniger\n"},
            {"Das kommt in die Wertstofftonne:", "-\talles was in den gelben Sack könnte\n" +
                    "  +\n" +
                    "-\tPlastik, Metall, Naturmaterialien die keine Verpackung sind\n" +
                    "-\tCDs, DVDs, Blue-Rays\n" +
                    "-\tDraht, Kabelreste, Nägel, Schrauben, Türgriff, Werkzeug\n" +
                    "-\tBlumentöpfe, Kochtöpfe\n" +
                    "-\tBackbleche, Backformen, Besteck\n" +
                    "-\tDuschvorhänge, Eimer, Haarbürste, Zahnbürste\n" +
                    "-\tPlastikspielzeug, Kleiderbügel, Locher\n" +
                    "-\tKlarsichtfolie, Spraydosen\n"},
            {"Das gehört NICHT in die Wertstofftonne:","-\tGlas, Papier, Pappe, Bioabfall\n" +
                    "\n" +
                    "-\tzu große Teile für die Tonne \n" +
                    "-\tGummi, Keramik, Holz, Textilien \n" +
                    "-\tVideo- und Musikkassetten\n" +
                    "-\tZelt\n" +
                    "-\tAutoteile, Fahrradschlauch\n" +
                    "-\tBauabfall, Dämmstoffe, Schaumstoff\n" +
                    "-\tElektroschrott, Batterien\n" +
                    "-\tChemikalien, Nadeln, Spritzen\n" +
                    "-\tHygieneartikel wie Binden, Windeln\n" +
                    "-\tBleistift, Klebeband, Wachspapiere, Watte\n"},
            {"Das kommt in die Biotonne:","-\tLebensmittel, Teebeutel\n" +
                    "-\tGartenabfälle\n" +
                    "-\tHeu, Stroh\n" +
                    "-\tTopfpflanzen ohne Topf, Schnittblumen\n" +
                    "-\tFedern, Haare\n" +
                    "-\tHolzwolle und Holzspäne von unbehandeltem Holz\n" +
                    "-\tBioabfallsammeltüten aus Papier\n"},
            {"Das gehört NICHT in die Biotonne:","-\tAsche, Ruß, Staubsaugerbeutel\n" +
                    "-\tBlumentöpfe, Draht\n" +
                    "-\tVerpackungen aus zertifiziert biologisch abbaubarem Plastik etc. \n" +
                    "-\tWatte, Windeln\n" +
                    "-\tKatzenstreu – je nach Art und Wohnort\n"},
            {"Das kommt in den Restmüll:","-\tHygieneartikel wie Windeln, Binden\n" +
                    "-\tKatzenstreu, Streusand aus Vogelkäfigen\n" +
                    "-\tTextilien\n" +
                    "-\tRasierklingen, Watte, Taschentücher, verschmutzte Papiere\n" +
                    "-\tKohle, Streichhölzer, Asche, Zigaretten\n" +
                    "-\tBehandeltes Holz (Reste)\n"},
            {"Das gehört NICHT in den Restmüll:","-\tChemikalien\n" +
                    "-\tBauschutt\n" +
                    "-\tElektroschrott, Batterien, Energiesparlampen\n" +
                    "-\tSperrmüll\n"},
            {"Definition von Sperrmüll:","Abfälle zur Beseitigung aus privaten Haushalten, die\n" +
                    "-\tnicht von der Entsorgung im Holsystem ausgeschlossen sind \n" +
                    "-\tinfolge ihrer Größe oder ihres Gewichts nicht in die zugelassenen Abfallbehältnisse aufgenommen werden können \n" +
                    "-\tdas Entleeren dieser Tonnen/Säcke erschweren \n"},
            //fett machen
            {"Das kommt in den Sperrmüll: ","Metall-Schrott: Fahrrad, Lattenroste mit Metallfedern, Kinderwagen, Ölofen (erkennbar ölfrei), Rasenmäher (Öl-/Benzinfrei), Metallregale\n" +
                    "\n" +
                    "Sperrmüll-Altholz: Holzmöbel defekt\n" +
                    "\n" +
                    "Elektro-Schrott: Bildschirme, Computer, Fernsehgeräte, Herd, Kühl-/Gefriergeräte, Geschirrspüler, Waschmaschinen, Wäschetrockner\n" +
                    "\n" +
                    "Rest-Sperrmüll: Feder/Daunenbetten, Autofußmatten, sperrige Haushaltskunststoffe, Gartenmöbel, große Kinderspielsachen, Kindersitz, Koffer, Matratzen, Polstermöbel, Ski, Skibox, Teppiche\n"},

            //Mythen
            {"Mythos 1: ","Der in gelbe Tonnen oder Säcke geworfene Hausmüll könne dank innovativer Technik einfach wieder aussortiert werden. \n" +
                    "Dem ist nicht so. \n\n" +
                    "Mit Störstoffen kontaminierte Leichtverpackungsabfälle sind in den allermeisten Fällen für das Recycling verloren und können nur noch verbrannt werden.\n"},
            //Tipps
                {"Tipp: Verpackungsteile einzeln wegwerfen","Viele Verpackungsteile sind aus verschiedenen Materialien und können nur getrennt von der Maschine sortiert werden.\n" +
                    "\n" +
                    "\tz.B.\n" +
                    "-\tTetrapack und Deckel\n" +
                    "-\tPlastikbecher und Deckel\n" +
                    "-\tFoliendeckel und Verpackung von Wurst, Käse etc.\n"},
            {"Tipp: Bioabfall in Papiertüten oder Zeitung einwickeln oder sammeln.","Biologisch abbaubare Plastiktüten sollten Sie NICHT verwenden. Diese bauen sich zu langsam ab."}
    };

    public int getNewsCount() {
        try {
            newsRowNumber = ResponseManager.getNewsfeedRows();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return newsRowNumber;
    }

        public String[] getSingleNews(int newsIndex){
        try {
            singleNewsArray  = ResponseManager.getNewsfeedData(newsIndex);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return singleNewsArray;
    }
}
