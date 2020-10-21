import { mdiComment, mdiCommentOutline, mdiCube, mdiCubeOutline, mdiEllipse, mdiEllipseOutline, mdiFolderDownload, mdiFolderDownloadOutline, mdiHome, mdiHomeOutline, mdiShape, mdiShapeOutline } from '@mdi/js';

export default [
	{
		text: 'Home',
		icon: mdiHomeOutline,
		openIcon: mdiHome,
		href: ''
	},
	{
		text: 'Context',
		icon: mdiCommentOutline,
		openIcon: mdiComment,
		href: 'context'
	},
	{
		text: 'or Context with same icon as schemata-context',
		icon: mdiEllipseOutline,
		openIcon: mdiEllipse,
		href: 'context'
	},
	{
		text: 'Model',
		icon: mdiShapeOutline,
		openIcon: mdiShape,
		href: 'model'
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