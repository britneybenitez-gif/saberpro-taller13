import { JSDOM } from 'jsdom';
import fs from 'node:fs';
const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>');
globalThis.window=dom.window;
globalThis.document=dom.window.document;
const {default:mermaid}=await import('mermaid');
mermaid.initialize({startOnLoad:false});
const results=[];
for (const diagram of JSON.parse(fs.readFileSync('diagrams.json','utf8'))) {
  try {
    const parsed=await mermaid.parse(diagram.source);
    results.push({file:diagram.file,block:diagram.block,valid:true,type:parsed.diagramType});
  } catch(error) { results.push({file:diagram.file,block:diagram.block,valid:false,error:error.message}); }
}
const version=JSON.parse(fs.readFileSync('node_modules/mermaid/package.json','utf8')).version;
fs.writeFileSync('results.json',JSON.stringify({version,results},null,2));
console.log(JSON.stringify({version,results},null,2));
