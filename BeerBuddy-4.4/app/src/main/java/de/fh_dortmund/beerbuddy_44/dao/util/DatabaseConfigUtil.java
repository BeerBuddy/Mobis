package de.fh_dortmund.beerbuddy_44.dao.util;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;

/**
 * DatabaseConfigUtl writes a configuration file to avoid using annotation processing in runtime which is very slow
 * under Android. This gains a noticeable performance improvement.
 * 
 * The configuration file is written to /res/raw/ by default. More info at: http://ormlite.com/docs/table-config
 */

public final class DatabaseConfigUtil extends OrmLiteConfigUtil {

	private static final Class<?>[] classes = new Class[] {
			Person.class, FriendList.class, FriendInvitation.class, DrinkingSpot.class, DrinkingInvitation.class
	};

	public static void main(String[] args) throws SQLException, IOException {
		System.out.println("Writing Config File");
		writeConfigFile("ormlite_config.txt",classes);
	}
}