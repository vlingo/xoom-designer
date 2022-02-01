const errors = Object.freeze({
	EMPTY: "may not be empty",
	VERSION: "Must be a semantic version number (major.minor.patch, e.g. 1.6.12)",
	CLASSNAME: "must be initial cap (e.g. SomethingDefined)",
	NAMESPACE: "Must be namespace syntax (e.g. com.example.demo)",
	ARTIFACT: "Must consist of lowercase letters and hyphens",
	FIELDNAME: "must follow java identifier pattern",
	ROUTEPATH: "Must follow route path pattern",
	ROOTPATH: "Must follow root path pattern",
	XOOMVERSION: "Must be either 1.4.1-SNAPSHOT or 1.4.0",
	SCHEMAGROUP: "Must consist of organization, unit and context separated by colon",
	SCHEMA: "Must consist of organization, unit, context, name and semantic version separated by colon",
	PROJECTNAME: "Must be formatted like MyCompany.MyTechnology.FirstFeature"
	// SUBMIT: "Fields may not be empty",
	// SUBMITVER: "Previous and current version must be a semantic version number (major.minor.patch, e.g. 1.6.12)",
	// SUBMITVEREXISTS: "The chosen current version already exists"
});

const frameworks = [
	'netcoreapp 1.0',
	'netcoreapp 1.1',
	'netcoreapp2.0',
	'netcoreapp 2.1',
	'netcoreapp 2.2',
	'netcoreapp 3.0',
	'netcoreapp 3.1',
	'NET 5.0 *',
	'Netstandard 1.0',
	'Netstandard 1.1',
	'Netstandard 1.2',
	'Netstandard 1.3',
	'Netstandard 1.4',
	'Netstandard 1.5',
	'Netstandard 1.6',
	'Netstandard 2.0',
	'Netstandard 2.1',
	'net11',
	'net20',
	'net35',
	'net40',
	'net403',
	'net45',
	'net451',
	'net452',
	'net46',
	'net461',
	'net462',
	'net47',
	'net471',
	'net472',
	'net48',
	'netmf'
]

export const requireRule = (value) => !!value ? undefined : errors.EMPTY;

export const versionRule = (value) => /^\d+\.\d+\.\d+$/.test(value) ? undefined : errors.VERSION;
export const packageRule = (value) => /^[a-z]+(\.[a-zA-Z_]([a-zA-Z_$#\d])*)+$/.test(value) ? undefined : errors.NAMESPACE;
export const artifactRule = (value) => /^[a-z-]+$/.test(value) ? undefined : errors.ARTIFACT;
export const classNameRule = (value) => /^[A-Z]+[A-Za-z]*$/.test(value) ? undefined : errors.CLASSNAME;
export const identifierRule = (value) => /^[a-zA-Z_$][a-zA-Z_$0-9]*$/.test(value) ? undefined : errors.FIELDNAME;
export const routeRule = (value) => /(^\*|(^\/?(([0-9a-zA-Z\-]+|{[0-9a-zA-Z]+})\/?)+))$/.test(value) ? undefined : errors.ROUTEPATH;
export const rootPathRule = (value) => /(^\/{1}(([0-9a-zA-Z\-]+|{[0-9a-zA-Z]+})\/?)+)$/.test(value) ? undefined : errors.ROOTPATH;
export const xoomVersionRule = (value) => ["1.4.1-SNAPSHOT", "1.4.0"].some(v => v == value) ? undefined : errors.XOOMVERSION;
export const schemaGroupRule = (value) => /^([A-Za-z]+\.*)+\:[A-Za-z]+\:([a-z_]\d*(\.[a-z_])?)+$/.test(value) ? undefined : errors.SCHEMAGROUP;
export const schemaRule = (value) => /^[A-Za-z]+\:[A-Za-z]+\:[A-Za-z.]+\:[A-Za-z]+\:\d+\.\d+\.\d+$/.test(value) ? undefined : errors.SCHEMA;
export const isPropertyUniqueRule = (value, array, prop) => array.filter(obj => obj[prop] === value).length === 1 ? undefined : 'must be unique';
export const isAggregateUniqueRule = (oldAggregate, aggregateName, aggregateSettings) => aggregateSettings.filter(a => JSON.stringify(a) !== JSON.stringify(oldAggregate) && a.aggregateName === aggregateName).length === 0 ? undefined : `Aggregate name must be unique`;
export const pathShouldHaveId = (v) => /\{id\}/.test(v) ? undefined : 'Must have {id}';
export const projectNameRule = (value) => /^([A-Z]+[A-Za-z]*.){1,2}[A-Za-z]+$/.test(value) ? undefined : errors.PROJECTNAME;
export const frameworkRule = (v) => frameworks.includes(v) ? undefined : 'framework should match one of the available frameworks here! https://docs.microsoft.com/fr-fr/dotnet/standard/frameworks'
export const methodParametersValidityWithSelectedEventRule = (event, events, parameters) => {
	const e = events.find(e => e.name === event);
	if (e) {
		const check = parameters.every(p => {
			return e.fields.findIndex(f => f === p.stateField) > -1;
		})
		return check ? undefined : 'do not match the event fields';
	}
	return undefined;
};
export const eventAlreadyInUseRule = (method, methods) => {
	if(!method.event) {
		return undefined;
	}
	const check = methods.some(m => {
		return method.name != m.name && m.event === method.event;
	});
	return !check ? undefined : 'is already in use by other method';
};