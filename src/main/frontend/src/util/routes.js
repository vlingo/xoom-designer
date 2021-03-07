import { mdiComment, mdiCommentOutline, mdiShapeOvalPlus, mdiCube, mdiCubeOutline, mdiDatabase, mdiDatabaseOutline, mdiFolderDownload, mdiFolderDownloadOutline, mdiShape, mdiShapeOutline } from '@mdi/js';

export default [
	// {
	// 	text: 'Home',
	// 	icon: mdiHomeOutline,
	// 	openIcon: mdiHome,
	// 	href: ''
	// },
	{
		text: 'Context',
		icon: mdiCommentOutline,
		openIcon: mdiComment,
		href: 'context'
	},
	{
		text: 'Object Values',
		icon: mdiShapeOvalPlus,
		openIcon: mdiShapeOvalPlus,
		href: 'object-values'
	},
	// {
	// 	text: 'or Context with same icon as schemata-context',
	// 	icon: mdiEllipseOutline,
	// 	openIcon: mdiEllipse,
	// 	href: 'context'
	// },
	{
		text: 'Aggregates',
		icon: mdiShapeOutline,
		openIcon: mdiShape,
		href: 'aggregates'
	},
	{
		text: 'Persistence',
		icon: mdiDatabaseOutline,
		openIcon: mdiDatabase,
		href: 'persistence'
	},
	{
		text: 'Deployment',
		icon: mdiCubeOutline,
		openIcon: mdiCube,
		href: 'deployment'
	},
	{
		text: 'Generation',
		icon: mdiFolderDownloadOutline,
		openIcon: mdiFolderDownload,
		href: 'generation'
	},
];