/*
 * Copyright (C) 2012  Benjamin Boksa (http://www.boksa.de/)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package de.boksa.rt.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import de.boksa.rt.dao.RESTRTDAOFactory;
import de.boksa.rt.dao.RTTicketDAO;
import de.boksa.rt.model.RTTicket;

public class RESTRTTicketDaoBasicAuthTest {

    // use Commons Logging
    private static final Log LOG = LogFactory.getLog(RESTRTTicketDaoBasicAuthTest.class);

    // this is a RT Query Builder query
    private static final String RTQBQ_ALL_FROM_CUSTOMER_SERVICE = "Queue = 'Customer Service'";

    // this is rather a demo than a test ;-)
    @Test
    public void demo() {
        // we use a Map to hold the factory parameters to have a common
        // signature for the factory method
        Map<String, Object> factoryParameters = new HashMap<String, Object>();

        // for the credentials used see
        // http://requesttracker.wikia.com/wiki/Demo
        // this RT instance actually isn't protected by BASIC auth. This should
        // be pointed at an instance that is
        // for a "real" test.
        LOG.debug("Setting credentials to access the RT demo installation");

        ResourceBundle bundle = ResourceBundle.getBundle("test");

        factoryParameters.put(RESTRTDAOFactory.REST_INTERFACE_BASE_URL, bundle.getString("baseUrl"));
        factoryParameters.put(RESTRTDAOFactory.REST_INTERFACE_USERNAME, bundle.getString("user"));
        factoryParameters.put(RESTRTDAOFactory.REST_INTERFACE_PASSWORD, bundle.getString("pass"));
        factoryParameters.put(RESTRTDAOFactory.REST_INTERFACE_AUTH_TYPE, RESTRTDAOFactory.AUTH_TYPE_BASIC);

        LOG.debug("Creating the RTTicketDAO");
        RTTicketDAO dao = RESTRTDAOFactory.getInstance().getRTTicketDAO(factoryParameters);

        // Strictly following the J2EE DAO Pattern that would be:
        // RTTicketDAO dao =
        // RTDAOFactory.getRTDAOFactory(RTDAOFactoryType.REST).getRTTicketDAO(factoryParameters);

        try {
            LOG.debug("Running the RT Query Builder query and parsing the results");
            List<RTTicket> result = dao.findByQuery(RTQBQ_ALL_FROM_CUSTOMER_SERVICE);

            LOG.debug("Found " + result.size() + " tickets");
            if (result.size() > 0) {
                LOG.debug("Iterating over the resulting POJOs");
                for (RTTicket ticket : result) {
                    LOG.debug("   Found ticket: " + ticket.getId() + ", " + ticket.getSubject());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @Test
//    public void ticketById() {
//        
//    }

}
