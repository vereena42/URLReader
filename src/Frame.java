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

import com.j256.ormlite.dao.Dao;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Frame extends JFrame {

    private List<String> history=new ArrayList<String>();
    private Box box=null;
    private JPanel container;
    private JTextField jTextField;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel message=new JLabel("");
    private JButton b;
    private Dao<Site,String> sites;
    Dao<SiteLink,Site>siteLinks;
    public Frame(Dao<Site,String> sites,Dao<SiteLink,Site> siteLinks) {
        super("URLReader");
        this.sites=sites;
        this.siteLinks=siteLinks;
        b=new JButton("Back");
        b.addActionListener(new ButtonActionListener(this));
        container = new JPanel();
        jTextField=new JTextField(30);
        jTextField.addActionListener(new TextFieldActionListener(this));
        container.add(message,LEFT_ALIGNMENT);
        container.add(jTextField, LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(500, 500));
        setSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500,500));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane jsp = new JScrollPane(container);
        add(jsp);
        setVisible(true);


    }
    public void changeMessage(String message){
        this.message.setText(message);
        repaint();
        setVisible(true);
    }

    public void goBack() throws IOException, SQLException {
        if(history.size()==1) {
            this.message.setText("You can't go back. It's the first address!");
            repaint();
            setVisible(true);
        }
        else
        {
            history.remove(history.size()-1);
            String url=history.get(history.size()-1);
            changeTo(url,true);
        }
    }
    public void changeTo(String url,boolean back) throws IOException, SQLException {
        Site s=sites.queryForId(url);
        List<String>links;
        int imagesNumber;
        int imagesSize;
        if(s!=null)
        {
            if(!back)
                history.add(url);
            Collection<SiteLink> list=s.getLinks();
            links=new ArrayList<String>();
            for(SiteLink siteLink: list)
            {
                links.add(siteLink.getChild());
            }
            imagesNumber=s.getImgSum();
            imagesSize=s.getImgWeight();
        }
        else {
            URLReader urlReader = new URLReader(url);
            if(!back)
                history.add(url);
            links=urlReader.getLinks();
            imagesNumber=urlReader.getImagesNumber();
            imagesSize=urlReader.getImagesSize();
            Site site=new Site(url,imagesNumber,imagesSize);
            SiteLink siteLink;
            List<SiteLink>foreignList=new ArrayList<SiteLink>();
            for(String link: links) {
                siteLink=new SiteLink(site,link);
                siteLinks.create(siteLink);
                foreignList.add(siteLink);
            }
            site.setLinks(foreignList);
            sites.create(site);
            siteLinks.commit(siteLinks.getConnectionSource().getReadWriteConnection());
            sites.commit(sites.getConnectionSource().getReadWriteConnection());
        }
        if(this.box!=null)
        {
            container.setVisible(false);
            label1.setText(url);
            label2.setText("Obrazków na stronie: "+imagesNumber);
            label3.setText("Waga obrazków: "+imagesSize+" b");
            container.remove(this.box);
            this.box=Box.createVerticalBox();
            message.setText("");
            box.add(message,LEFT_ALIGNMENT);
            box.add(label1,LEFT_ALIGNMENT);
            box.add(label2,LEFT_ALIGNMENT);
            box.add(label3,LEFT_ALIGNMENT);
            box.add(b,LEFT_ALIGNMENT);
            BoxOfButtons boxOfButtons=new BoxOfButtons(links,this);
            box.add(boxOfButtons,LEFT_ALIGNMENT);
            box.setAlignmentX(0);
            container.add(box,LEFT_ALIGNMENT);
            container.repaint();
            container.setVisible(true);
            setVisible(true);
        }
        else
        {
            container.setVisible(false);
            container.remove(jTextField);
            container.remove(message);
            label1=new JLabel(url);
            label2=new JLabel("Obrazków na stronie: "+imagesNumber);
            label3=new JLabel("Waga obrazków: "+imagesSize+" b");
            Box box=Box.createVerticalBox();
            message.setText("");
            box.add(message,LEFT_ALIGNMENT);
            box.add(label1,LEFT_ALIGNMENT);
            box.add(label2,LEFT_ALIGNMENT);
            box.add(label3,LEFT_ALIGNMENT);
            box.add(b,LEFT_ALIGNMENT);
            BoxOfButtons boxOfButtons=new BoxOfButtons(links,this);
            box.add(boxOfButtons,LEFT_ALIGNMENT);
            this.box=box;
            container.add(box,LEFT_ALIGNMENT);
            container.repaint();
            container.setVisible(true);
            setVisible(true);
        }
    }

}
