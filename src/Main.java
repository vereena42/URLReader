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
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.awt.*;
import java.sql.SQLException;

public class Main {
    public static void main(String [] args) throws SQLException {
        final ConnectionSource connectionSource=new JdbcConnectionSource("jdbc:sqlite:mydatabase.db");
        TableUtils.createTableIfNotExists(connectionSource, Site.class);
        TableUtils.createTableIfNotExists(connectionSource, SiteLink.class);
        final Dao<Site,String>sites= DaoManager.createDao(connectionSource,Site.class);
        final Dao<SiteLink,Site>siteLinks=DaoManager.createDao(connectionSource, SiteLink.class);
        sites.setAutoCommit(connectionSource.getReadWriteConnection(),false);
        siteLinks.setAutoCommit(connectionSource.getReadWriteConnection(),false);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frame(sites,siteLinks);
            }
        });
    }
}
