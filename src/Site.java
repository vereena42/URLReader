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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "sites")
public class Site {
    @DatabaseField(id = true,canBeNull = false)
    private String url;
    @DatabaseField(canBeNull = false)
    private int imgSum;
    @DatabaseField(canBeNull = false)
    private int imgWeight;
    @ForeignCollectionField
    Collection<SiteLink> links;

    public Site(){

    }

    public Site(String url,int imgSum,int imgWeight){
        this.url=url;
        this.imgSum=imgSum;
        this.imgWeight=imgWeight;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public int getImgSum(){
        return this.imgSum;
    }

    public void setImgSum(int imgSum){
        this.imgSum=imgSum;
    }

    public int getImgWeight() {
        return this.imgWeight;
    }

    public void setImgWeight(int imgWeight) {
        this.imgWeight = imgWeight;
    }

    public void setLinks(Collection<SiteLink> links) {
        this.links = links;
    }

    public Collection<SiteLink> getLinks() {
        return links;
    }
}
