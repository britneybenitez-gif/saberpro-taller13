from pathlib import Path
import json,time,urllib.request,urllib.error
out=Path('docs/auditoria/evidencias');results=[]
def req(port,path,body=None):
    r=urllib.request.Request(f'http://localhost:{port}{path}',data=None if body is None else json.dumps(body).encode(),headers={'Content-Type':'application/json'},method='GET' if body is None else 'POST')
    try:response=urllib.request.urlopen(r,timeout=10)
    except urllib.error.HTTPError as ex:response=ex
    with response:
        raw=response.read().decode()
        return response.status,json.loads(raw) if raw else None
valid=json.loads(Path('ejemplo-pregunta.json').read_text())
for name,field in [('enunciado','enunciado'),('opcion','opciones')]:
    payload=json.loads(json.dumps(valid))
    if field=='opciones':payload[field][0]['texto']='\u0000\t '
    else:payload[field]='\u0000\t '
    for mode,port in [('jpa',8083),('memory',18083)]:
        status,body=req(port,'/api/preguntas',payload)
        item={'case':name,'repository':mode,'input':payload,'status':status,'response':body}
        if status==201:
            qid=body['id']
            item['reviewStatus'],item['reviewBody']=req(port,f'/api/preguntas/{qid}/enviar-revision',{})
            for _ in range(50):
                status2,body2=req(8084,'/api/revisiones/'+qid)
                if status2==200:break
                time.sleep(.2)
            item['lifecycleStatus']=status2;item['receipt']=body2
        results.append(item)
        print(name,mode,status,'review',item.get('reviewStatus'),'lifecycle',item.get('lifecycleStatus'),flush=True)
(out/'http-normalizacion.json').write_text(json.dumps(results,ensure_ascii=False,indent=2)+'\n')
(out/'DomainBoundaryProbe.java').write_text(Path('/tmp/t13-audit-domain/DomainBoundaryProbe.java').read_text())
