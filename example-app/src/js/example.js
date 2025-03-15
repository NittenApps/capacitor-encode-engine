import { EncodeEngine } from '@nittenapps/encode-engine';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    EncodeEngine.echo({ value: inputValue })
}
