

async function login(req, res) {
    const { email, password } = req.body;

    try {

    } catch (error) {
        console.error({ error });

        if (error.missingInput)
            return res
                .status(error.status || 500)
                .json({ missingInput: error.missingInput });
        return res.status(error.status || 500).json({ error: error.message });
    }
}