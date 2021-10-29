async function Fetch(endpoint: string, init?: RequestInit): Promise<Response> {
	return await fetch(
		process.env.NODE_ENV === 'production' ? endpoint : `http://localhost${endpoint}`,
		init
	);
}

export default Fetch;
