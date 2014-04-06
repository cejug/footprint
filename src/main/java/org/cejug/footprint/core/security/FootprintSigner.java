/* - - - - - - - - - - - - - - - - - - - - -import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import com.lowagie.text.DocumentException;
option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 
 This file is part of the FOOTPRINT Project (a generator of signed PDF
 documents, originally used as JUG events certificates), hosted at
 https://github.com/cejug/footprint
 
 
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
package org.cejug.footprint.core.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import com.itextpdf.text.DocumentException;

/**
 * The class responsible to apply the digital signature inthe generated PDF
 * files.
 * 
 * @author Felipe Gaucho
 */
public interface FootprintSigner {
    /**
     * Signs a document with a private key.
     * 
     * @param input
     *            The input stream containing the bytes to be signed.
     * @param output
     *            The output signed document.
     * @throws IOException
     *             problems accessing the I/O streams.
     * @throws DocumentException
     *             Signature exception
     *             {@link com.lowagie.text.DocumentException}
     * @throws KeyStoreException
     *             {@link java.security.KeyStoreException}
     * @throws NoSuchAlgorithmException
     *             {@link java.security.NoSuchAlgorithmException}
     * @throws UnrecoverableKeyException
     *             {@link java.security.UnrecoverableKeyException}
     * 
     *             void sign(InputStream input, File output) throws IOException,
     *             FileNotFoundException, DocumentException, KeyStoreException,
     *             NoSuchAlgorithmException, UnrecoverableKeyException;
     */

    void sign(InputStream reader, OutputStream writer) throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, DocumentException;

}
