const errors = Object.freeze({
	EMPTY: "May not be empty",
	VERSION: "Must be a semantic version number (major.minor.patch, e.g. 1.6.12)",
	CLASSNAME: "Must be initial cap (e.g. SomethingDefined)",
	NAMESPACE: "Must be namespace syntax (e.g. com.example.demo)",
	ARTIFACT: "Must consist of lowercase letters and hyphens",
	FIELDNAME: "Must follow java identifier pattern",
	ROUTEPATH: "Must follow route path pattern",
	XOOMVERSION: "Must be either 1.4.1-SNAPSHOT or 1.4.0",
	SCHEMAGROUP: "Must consist of organization, unit and context separated by colon",
	SCHEMA: "Must consist of organization, unit, context, name and semantic version separated by colon"
	// SUBMIT: "Fields may not be empty",
	// SUBMITVER: "Previous and current version must be a semantic version number (major.minor.patch, e.g. 1.6.12)",
	// SUBMITVEREXISTS: "The chosen current version already exists"
});

export const requireRule = (value) => !!value ? undefined : errors.EMPTY;

export const versionRule = (value) => /^\d+\.\d+\.\d+$/.test(value) ? undefined : errors.VERSION;
export const packageRule = (value) => /^[a-z]+(\.[a-zA-Z_]([a-zA-Z_$#\d])*)+$/.test(value) ? undefined : errors.NAMESPACE;
export const artifactRule = (value) => /^[a-z-]+$/.test(value) ? undefined : errors.ARTIFACT;
export const classNameRule = (value) => /^[A-Z]+[A-Za-z]*$/.test(value) ? undefined : errors.CLASSNAME;
export const identifierRule = (value) => /^[a-zA-Z_$][a-zA-Z_$0-9]*$/.test(value) ? undefined : errors.FIELDNAME;
export const routeRule = (value) => /^[a-zA-Z_$/?%-]+$/.test(value) ? undefined : errors.ROUTEPATH;
export const xoomVersionRule = (value) => ["1.4.1-SNAPSHOT", "1.4.0"].some(v => v == value) ? undefined : errors.XOOMVERSION;
export const schemaGroupRule = (value) => /^[A-Za-z]+\:[A-Za-z]+\:[A-Za-z.]+$/.test(value) ? undefined : errors.SCHEMAGROUP;
export const schemaRule = (value) => /^[A-Za-z]+\:[A-Za-z]+\:[A-Za-z.]+\:[A-Za-z]+\:\d+\.\d+\.\d+$/.test(value) ? undefined : errors.SCHEMA;
export const isPropertyUnique = (value, store, prop) => store.filter(obj => obj[prop] === value).length === 1 ? undefined : `${prop.charAt(0).toUpperCase() + prop.slice(1)} must be unique`;