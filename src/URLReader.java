/*
	Copyright (c) 2016, Dominika Salawa <vereena42@gmail.com>
	All rights reserved.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:

		* Redistributions of source code must retain the above copyright notice,
		  this list of conditions and the following disclaimer.

		* Redistributions in binary form must reproduce the above copyright notice,
		  this list of conditions and the following disclaimer in the documentation
		  and/or other materials provided with the distribution.

		* Neither the name of the <organization> nor the names of its
		  contributors may be used to endorse or promote products derived from this
		  software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT,
	INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
	BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
	DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
	LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
	OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class URLReader {
    private HashSet<String> AllLinks=new HashSet<String>();
    private int HowManyImages=0;
    private int ImagesSize=0;
    private String url;
    public URLReader(String url) throws IOException {
        this.url=url;
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer all=new StringBuffer("");
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            all=all.append(inputLine);
        }
        in.close();

        Document doc = Jsoup.parse(all.toString());
        doc.setBaseUri(url);

        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");


        for(Element link: links) {
            String linkHref = link.attr("abs:href");
            if(linkHref.length()>3 && linkHref.charAt(0)=='h' && linkHref.charAt(1)=='t' && linkHref.charAt(2)=='t' && linkHref.charAt(3)=='p')
                this.AllLinks.add(linkHref);
        }

        HashSet<String>images=new HashSet<String>();
        for(Element link: media) {
            if(link.tagName().equals("img") && link.attr("abs:src").length()>0) {
                images.add(link.attr("abs:src"));
            }
        }
        int counter=0;
        for(String img: images){
            if(img.contains("http")) {
                URL url1 = new URL(img);
                this.ImagesSize += url1.openConnection().getContentLength();
                counter++;
            }
        }
        this.HowManyImages=counter;
    }

    public List<String> getLinks(){
        return new ArrayList<String>(this.AllLinks);
    }

    public int getImagesSize() {
        return this.ImagesSize;
    }

    public int getImagesNumber(){
        return this.HowManyImages;
    }

    public String getUrl(){
        return url;
    }
}
