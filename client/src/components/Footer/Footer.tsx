import facebook from "../../assets/icons/facebook.svg";
import instagram from "../../assets/icons/instagram.svg";
import linkedin from "../../assets/icons/linkedin.svg";
import maestro from "../../assets/icons/maestro.svg";
import mastercard from "../../assets/icons/mastercard.svg";
import paypal from "../../assets/icons/paypal.svg";
import visa from "../../assets/icons/visa.svg";
import x from "../../assets/icons/x.svg";
import logo from "../../assets/images/logo-light.svg";
import styles from "./Footer.module.scss";

const Footer: React.FC = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.footerMain}>
        <div className={styles.footerContent}>
          <div className={styles.footerBrand}>
            <div className={styles.brandInfo}>
              <img src={logo} alt="logo" className={styles.logo} />
              <p className={styles.description}>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                Pellentesque sit amet congue nisi, ultrices congue nisi. Aenean
                et placerat libero. Donec vitae auctor dolor, vitae blandit mi.
                Sed ultricies sapien mollis, dapibus arcu sed, euismod magna.
              </p>
            </div>
            <div className={styles.socialLinks}>
              <img
                src={facebook}
                alt="facebook"
                className={styles.socialIcon}
              />
              <img src={x} alt="x" className={styles.socialIcon} />
              <img
                src={instagram}
                alt="instagram"
                className={styles.socialIcon}
              />
              <img
                src={linkedin}
                alt="linkedin"
                className={styles.socialIcon}
              />
            </div>
          </div>
          <div className={styles.footerLinks}>
            <div className={styles.linksColumn}>
              <h4 className={styles.columnTitle}>Aide</h4>
              <ul className={styles.linksList}>
                <li className={styles.linkItem}>Une question ?</li>
                <li className={styles.linkItem}>
                  Conditions générales d'utilisation
                </li>
                <li className={styles.linkItem}>Devenir vendeur</li>
              </ul>
            </div>
            <div className={styles.linksColumn}>
              <h4 className={styles.columnTitle}>A propos</h4>
              <ul className={styles.linksList}>
                <li className={styles.linkItem}>Qui sommes nous ?</li>
                <li className={styles.linkItem}>
                  Conditions générales de ventes
                </li>
                <li className={styles.linkItem}>Données personnelles</li>
                <li className={styles.linkItem}>Mentions légales</li>
                <li className={styles.linkItem}>Cookies</li>
              </ul>
            </div>
            <div className={styles.linksColumn}>
              <h4 className={styles.columnTitle}>Catégories</h4>
              <ul className={styles.linksList}>
                <li className={styles.linkItem}>Fruits et légumes</li>
                <li className={styles.linkItem}>Fromages et lait</li>
                <li className={styles.linkItem}>Pains et pâtisseries</li>
                <li className={styles.linkItem}>Bio et équitables</li>
                <li className={styles.linkItem}>Produits artisanaux</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.footerBottom}>
        <p className={styles.copyright}>
          ComptoirLocal © 2024. Tous droits réservés
        </p>
        <div className={styles.paymentMethods}>
          <div className={styles.paymentIcon}>
            <img src={visa} alt="visa" />
          </div>
          <div className={styles.paymentIcon}>
            <img src={maestro} alt="maestro" />
          </div>
          <div className={styles.paymentIcon}>
            <img src={mastercard} alt="mastercard" />
          </div>
          <div className={styles.paymentIcon}>
            <img src={paypal} alt="paypal" />
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
