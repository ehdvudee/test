package jce.cert;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Vector;

import sun.security.util.ObjectIdentifier;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNames;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.SerialNumber;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.X500Name;

public class UserCertGenerator extends X509V3CertGenerator {

	public UserCertGenerator( CertGeneratorFactory.Builder builder ) {
		super(builder);
	}

	@Override
	public CertificateExtensions generateCertificateExtensions( PublicKey subjectPublicKey ) throws IOException {
		KeyUsageExtension keyUsage = new KeyUsageExtension(); 

		keyUsage.set( KeyUsageExtension.DIGITAL_SIGNATURE, true );
		keyUsage.set( KeyUsageExtension.KEY_ENCIPHERMENT, true );

		Vector<ObjectIdentifier> keyOid = new Vector<ObjectIdentifier>(); 
		keyOid.add( new ObjectIdentifier( new int[] { 1, 3, 6, 1, 5, 5, 7, 3, 1 }));	// server authentication
		keyOid.add( new ObjectIdentifier( new int[] { 1, 3, 6, 1, 5, 5, 7, 3, 2 }));	// client authentication 

		CertificateExtensions exts = new CertificateExtensions();

		exts.set( SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension( new KeyIdentifier( subjectPublicKey ).getIdentifier()));

		if ( getSubjectAltName() != null ) {
			exts.set( SubjectAlternativeNameExtension.NAME, new SubjectAlternativeNameExtension( false, getSubjectAltName() ));
		}

		exts.set( BasicConstraintsExtension.NAME, new BasicConstraintsExtension( false, 0 ) );
		
		exts.set( KeyUsageExtension.NAME, keyUsage ); 

		exts.set( ExtendedKeyUsageExtension.NAME, new ExtendedKeyUsageExtension( keyOid ));

		exts.set( AuthorityKeyIdentifierExtension.NAME,
				new AuthorityKeyIdentifierExtension(
						new KeyIdentifier( getIssuerCert().getPublicKey()),
						null,
						null
				));

		return exts;
	}

}
