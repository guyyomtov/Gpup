
package GpupClassesEx3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}GPUP-Configuration"/>
 *         &lt;element ref="{}GPUP-Targets"/>
 *         &lt;element name="GPUP-Serial-Sets" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="GPUP-Serial-set">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="targets" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "GPUP-Descriptor")
public class GPUPDescriptor {

    @XmlElement(name = "GPUP-Configuration", required = true)
    protected GPUPConfiguration gpupConfiguration;
    @XmlElement(name = "GPUP-Targets", required = true)
    protected GPUPTargets gpupTargets;
    @XmlElement(name = "GPUP-Serial-Sets")
    protected GPUPDescriptor.GPUPSerialSets gpupSerialSets;

    /**
     * Gets the value of the gpupConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link GPUPConfiguration }
     *     
     */
    public GPUPConfiguration getGPUPConfiguration() {
        return gpupConfiguration;
    }

    /**
     * Sets the value of the gpupConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link GPUPConfiguration }
     *     
     */
    public void setGPUPConfiguration(GPUPConfiguration value) {
        this.gpupConfiguration = value;
    }

    /**
     * Gets the value of the gpupTargets property.
     * 
     * @return
     *     possible object is
     *     {@link GPUPTargets }
     *     
     */
    public GPUPTargets getGPUPTargets() {
        return gpupTargets;
    }

    /**
     * Sets the value of the gpupTargets property.
     * 
     * @param value
     *     allowed object is
     *     {@link GPUPTargets }
     *     
     */
    public void setGPUPTargets(GPUPTargets value) {
        this.gpupTargets = value;
    }

    /**
     * Gets the value of the gpupSerialSets property.
     * 
     * @return
     *     possible object is
     *     {@link GPUPDescriptor.GPUPSerialSets }
     *     
     */
    public GPUPDescriptor.GPUPSerialSets getGPUPSerialSets() {
        return gpupSerialSets;
    }

    /**
     * Sets the value of the gpupSerialSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link GPUPDescriptor.GPUPSerialSets }
     *     
     */
    public void setGPUPSerialSets(GPUPDescriptor.GPUPSerialSets value) {
        this.gpupSerialSets = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="GPUP-Serial-set">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="targets" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "gpupSerialSet"
    })
    public static class GPUPSerialSets {

        @XmlElement(name = "GPUP-Serial-set", required = true)
        protected List<GPUPDescriptor.GPUPSerialSets.GPUPSerialSet> gpupSerialSet;

        /**
         * Gets the value of the gpupSerialSet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the gpupSerialSet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGPUPSerialSet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GPUPDescriptor.GPUPSerialSets.GPUPSerialSet }
         * 
         * 
         */
        public List<GPUPDescriptor.GPUPSerialSets.GPUPSerialSet> getGPUPSerialSet() {
            if (gpupSerialSet == null) {
                gpupSerialSet = new ArrayList<GPUPDescriptor.GPUPSerialSets.GPUPSerialSet>();
            }
            return this.gpupSerialSet;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="targets" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class GPUPSerialSet {

            @XmlAttribute(name = "targets", required = true)
            protected String targets;
            @XmlAttribute(name = "name", required = true)
            protected String name;

            /**
             * Gets the value of the targets property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTargets() {
                return targets;
            }

            /**
             * Sets the value of the targets property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTargets(String value) {
                this.targets = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }

}
